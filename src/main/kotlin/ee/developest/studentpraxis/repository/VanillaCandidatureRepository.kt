package ee.developest.studentpraxis.repository

import ee.developest.studentpraxis.model.VanillaCandidature
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface VanillaCandidatureRepository: JpaRepository<VanillaCandidature, Long> {
    fun findAllByAdvertisementId(advertisementId: Long): List<VanillaCandidature>
}