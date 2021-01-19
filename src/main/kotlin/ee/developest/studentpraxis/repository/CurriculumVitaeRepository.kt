package ee.developest.studentpraxis.repository

import ee.developest.studentpraxis.model.CurriculumVitae
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface CurriculumVitaeRepository: JpaRepository<CurriculumVitae, Long> {
    fun findAllByUserId(userId: Long): List<CurriculumVitae>
    fun findFirstByUserIdOrderByUploadTimeDesc(userId: Long): Optional<CurriculumVitae>
    fun findFirstByUserIdOrderByUploadTime(userId: Long): CurriculumVitae?
}