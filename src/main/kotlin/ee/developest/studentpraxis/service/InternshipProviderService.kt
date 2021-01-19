package ee.developest.studentpraxis.service

import ee.developest.studentpraxis.dto.request.InternshipProviderRequestDto
import ee.developest.studentpraxis.model.InternshipProvider
import ee.developest.studentpraxis.dto.response.InternshipProviderResponseDto
import ee.developest.studentpraxis.repository.InternshipProviderMemberRepository
import ee.developest.studentpraxis.repository.InternshipProviderRepository
import ee.developest.studentpraxis.security.JwtTokenProvider
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.util.*

@Service
class InternshipProviderService(
        private val internshipProviderRepository: InternshipProviderRepository,
        private val userService: UserService,
        private val internshipProviderMemberService: InternshipProviderMemberService,
        private val companyContactService: CompanyContactService,
        private val contactTypeService: ContactTypeService,
        private val fileService: FileService
) {

    fun getLastInternshipProviderPicture(internshipProviderId: Long): String? {
        return findByCompanyIdModel(internshipProviderId).image
    }

    fun getAll(): List<InternshipProvider> {
        return internshipProviderRepository.findAll()
    }

    fun findAllByUserId(userId: Long): List<InternshipProviderResponseDto> {
        val internshipProviderMemberList = internshipProviderMemberService.findAllByUserId(userId)
        var responseList: List<InternshipProviderResponseDto> = arrayListOf()
        for (ipm in internshipProviderMemberList) {
            responseList += findByCompanyId(ipm.internshipProvider.id)
        }
        return responseList
    }

    fun findByCompanyId(id: Long): InternshipProviderResponseDto {
        val phone = companyContactService.getByCompanyIdAndType(id, contactTypeService.getPhone())
        val email = companyContactService.getByCompanyIdAndType(id, contactTypeService.getEmail())
        val address = companyContactService.getByCompanyIdAndType(id, contactTypeService.getAddress())
        val ip = internshipProviderRepository.findById(id)

        ip.orElse(null) ?: return InternshipProviderResponseDto.empty()
        return InternshipProviderResponseDto.fromInternshipProvider(ip.get(), email.get(), phone.get(), address.get());
    }

    fun findByCompanyIdModel(id: Long): InternshipProvider {
        return internshipProviderRepository.findById(id).orElse(null) ?: throw Exception("Company not found")
    }

    fun save(userId: Long, internshipProviderRequestDto: InternshipProviderRequestDto, image: MultipartFile?): InternshipProviderResponseDto {
        val ip = internshipProviderRepository.save(InternshipProvider.fromInternshipProviderDto(internshipProviderRequestDto))


        if (image != null) {
            ip.image = fileService.saveInternshipProviderPicture(ip.id, image)
            internshipProviderRepository.save(ip)
        }

        ip.users.add(internshipProviderMemberService.addUserToInternshipProvider(userService.findUserById(userId), ip))

        val phone = companyContactService.save(ip, contactTypeService.getPhone(), internshipProviderRequestDto.phone)
        val email = companyContactService.save(ip, contactTypeService.getEmail(), internshipProviderRequestDto.email)
        val address = companyContactService.save(ip, contactTypeService.getAddress(), internshipProviderRequestDto.address)

        return InternshipProviderResponseDto.fromInternshipProvider(ip, email, phone, address);
    }
}