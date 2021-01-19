package ee.developest.studentpraxis.service

import ee.developest.studentpraxis.dto.InternshipCategoryDto
import ee.developest.studentpraxis.dto.request.AdvertisementRequestDto
import ee.developest.studentpraxis.dto.response.AdvertisementDto
import ee.developest.studentpraxis.dto.response.InternshipProviderResponseDto
import ee.developest.studentpraxis.model.Advertisement
import ee.developest.studentpraxis.repository.AdvertisementRepository
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashSet

@Service
class AdvertisementService(
        private val advertisementRepository: AdvertisementRepository,
        private val internshipProviderService: InternshipProviderService,
        private val invoiceService: InvoiceService,
        private val advertisementInvoiceMemberService: AdvertisementInvoiceMemberService,
        private val categoryService: InternshipCategoryService,
        private val internshipCategoryMemberService: InternshipCategoryMemberService,
        private val dealService: DealService,
        private val companyContactService: CompanyContactService,
        private val contactTypeService: ContactTypeService,
        private val userService: UserService,
        private val internshipProviderMemberService: InternshipProviderMemberService,
        private val fileService: FileService) {

    fun getAdvertisementsByInternshipProviderId(id: Long): List<AdvertisementDto> {
        return advertisementRepository.findDistinctByInternshipProviderIdOrderByCreatedTimestampDesc(id)
                .map { ad -> generateAdvertisementDtoFromAdvertisement(ad) }
    }

    fun getAdvertisementsByUserId(userId: Long): List<AdvertisementDto> {
        val userInternshipProviderMemberList = internshipProviderMemberService.findAllByUserId(userId)
        var advertisements = ArrayList<Advertisement>()
        for (userInternshipProviderMember in userInternshipProviderMemberList) {
            advertisements.addAll(advertisementRepository.findAllByInternshipProvider(userInternshipProviderMember.internshipProvider))
        }
        return advertisements.map { ad -> generateAdvertisementDtoFromAdvertisement(ad) }
    }

    fun isActive(advertisement: Advertisement): Boolean {
        return advertisement.startTimestamp != null && advertisement.endTimestamp != null && advertisement.endTimestamp!!.after(Date())
    }

    fun getAdvertisementByAdvertisementId(id: Long): Advertisement {
        return advertisementRepository.findById(id).get()
    }

    fun getAllActiveAdvertisements(): List<AdvertisementDto> {
        return findAdvertisementsByAdvertisementIds(advertisementRepository.findAllActive().toHashSet())
                .map { ad -> generateAdvertisementDtoFromAdvertisement(ad) }
                .sortedByDescending { it.createdStamp }
    }

    fun getAdvertisementsByCategories(categories: List<Long>): List<AdvertisementDto> {
        val activeAdvertisementIds: HashSet<Long> = HashSet()
        for (id in categories) {
            activeAdvertisementIds.addAll(advertisementRepository.findActiveByCategoryId(id))
        }
        return findAdvertisementsByAdvertisementIds(activeAdvertisementIds)
                .map { ad -> generateAdvertisementDtoFromAdvertisement(ad) }
                .sortedByDescending { it.createdStamp }
    }

    fun findAdvertisementsByAdvertisementIds(advertisementIds: HashSet<Long>): ArrayList<Advertisement> {
        val activeAdvertisements: ArrayList<Advertisement> = ArrayList()
        for (id in advertisementIds) {
            val ad = advertisementRepository.findById(id)
                    .orElse(null) ?: throw Exception("Advertisement not found!")
            activeAdvertisements.add(ad)
        }

        return activeAdvertisements
    }

    fun saveAdvertisement(advertisementRequestDto: AdvertisementRequestDto, img: MultipartFile?): AdvertisementDto {
        val ip = internshipProviderService.findByCompanyIdModel(advertisementRequestDto.companyId)
        val advertisement = advertisementRepository.save(
                Advertisement.fromAdvertisementDto(advertisementRequestDto, ip))
        if (img != null) {
            advertisement.image = fileService.saveAdvertisementPicture(advertisement, img)
            advertisementRepository.save(advertisement)
        }
        advertisementInvoiceMemberService.save(
                advertisement,
                invoiceService.generateInvoice(advertisementRequestDto.companyId, advertisementRequestDto.deal, listOf(advertisement)))
         for (categoryId in advertisementRequestDto.categories) {
             internshipCategoryMemberService.save(advertisement, categoryService.findById(categoryId).get())
         }
        return generateAdvertisementDtoFromAdvertisement(advertisement)
    }

    fun generateAdvertisementDtoFromAdvertisement(advertisement: Advertisement): AdvertisementDto {
        val adDto = AdvertisementDto.fromAdvertisement(advertisement)
        val ipDto = InternshipProviderResponseDto.fromInternshipProvider(
                internshipProviderService.findByCompanyIdModel(advertisement.internshipProvider.id),
                companyContactService.getByCompanyIdAndType(advertisement.internshipProvider.id, contactTypeService.getEmail()).get(),
                companyContactService.getByCompanyIdAndType(advertisement.internshipProvider.id, contactTypeService.getPhone()).get(),
                companyContactService.getByCompanyIdAndType(advertisement.internshipProvider.id, contactTypeService.getAddress()).get()
        )
        val categoryDtoList = ArrayList<InternshipCategoryDto>()
        for (member in internshipCategoryMemberService.findAllByAdvertisementId(advertisement.id)) {
            categoryDtoList.add(InternshipCategoryDto.fromInternshipCategory(categoryService.findById(member.internshipCategory.id).get()))
        }

        adDto.internshipProvider = ipDto
        adDto.categories = categoryDtoList
        return adDto
    }

    fun findModelById(id: Long): Advertisement {
        return advertisementRepository.findById(id).orElse(null) ?: throw Exception("Advertisement not found")
    }

    fun update(advertisement: Advertisement): Advertisement {
        return advertisementRepository.save(advertisement)
    }


}