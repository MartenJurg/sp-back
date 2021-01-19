package ee.developest.studentpraxis.service

import ee.developest.studentpraxis.dto.CandidatureDto
import ee.developest.studentpraxis.dto.VanillaCandidatureDto
import ee.developest.studentpraxis.model.*
import ee.developest.studentpraxis.repository.CandidatureRepository
import ee.developest.studentpraxis.repository.CurriculumVitaeRepository
import ee.developest.studentpraxis.repository.VanillaCandidatureRepository
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*
import kotlin.collections.ArrayList

@Service
class CandidateService(
        private val candidatureRepository: CandidatureRepository,
        private val vanillaCandidatureRepository: VanillaCandidatureRepository,
        private val advertisementService: AdvertisementService,
        private val internService: InternService,
        private val userContactService: UserContactService,
        private val userContactTypeService: ContactTypeService,
        private val curriculumVitaeRepository: CurriculumVitaeRepository
) {

    fun getCandidature(userId: Long, advertisementId: Long): List<CandidatureDto> {
        return candidatureRepository.findAllByAdvertisementId(advertisementId)
                .map {
                    candidature -> CandidatureDto.fromModelAndContact(
                        candidature,
                        curriculumVitaeRepository.findFirstByUserIdOrderByUploadTime(userId)?.file,
                        userContactService.findLastByUserIdAndType(userId, userContactTypeService.getLinkedIn())?.value)
                }
    }

    fun getVanillaCandidature(userId: Long, advertisementId: Long): List<VanillaCandidatureDto> {
        return vanillaCandidatureRepository.findAllByAdvertisementId(advertisementId)
                .map { vanillaCandidature -> VanillaCandidatureDto.fromModel(vanillaCandidature) }
    }

    fun save(userId: Long, advertisementId: Long): Candidature {
        val advertisement = advertisementService.findModelById(advertisementId)
        if (!advertisementService.isActive(advertisement)) throw Exception("Advertisement is not active")
        if (candidatureRepository.findAllByInternIdAndAdvertisementId(userId, advertisementId).size != 0) throw throw Exception("Already candidated")
        return candidatureRepository.save(Candidature(
                timestamp = Date(),
                intern = internService.findById(userId),
                advertisement = advertisement))
    }

    fun saveVanillaCandidature(advertisementId: Long, email: String, linkedin: String, cv: MultipartFile?) {
        val vanillaCandidature = vanillaCandidatureRepository.save(VanillaCandidature(
                advertisement = advertisementService.findModelById(advertisementId),
                email = email,
                linkedin = linkedin,
                CV = "",
                timestamp = Date()
        ))

        if (cv == null) {
            return
        }

        val date = Date()
        val pathString = "/files/vanilla_cv/${vanillaCandidature.id}/"
        val filename = date.time.toString() + "." + (cv.originalFilename?.split(".")?.last() ?: throw Exception("CV uploading failed"))

        File(pathString).mkdirs()
        Files.write(Paths.get(pathString + filename), cv.bytes)
        vanillaCandidature.CV = pathString + filename

        vanillaCandidatureRepository.save(vanillaCandidature)
    }
}