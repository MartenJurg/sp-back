package ee.developest.studentpraxis.repository

import ee.developest.studentpraxis.model.InternshipCategory
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface InternshipCategoryRepository: JpaRepository<InternshipCategory, Long> {
}