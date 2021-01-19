package ee.developest.studentpraxis.repository

import ee.developest.studentpraxis.model.Candidature
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CandidatureRepository: JpaRepository<Candidature, Long> {
    fun findAllByInternIdAndAdvertisementId(userId: Long, advertisementId: Long): List<Candidature>
    fun findAllByAdvertisementId(advertisementId: Long): List<Candidature>
}