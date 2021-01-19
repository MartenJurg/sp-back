package ee.developest.studentpraxis.repository

import ee.developest.studentpraxis.model.member.InternshipCategoryMember
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface InternshipCategoryMemberRepository: JpaRepository<InternshipCategoryMember, Long> {
    fun findAllByInternshipCategoryId(id: Long): List<InternshipCategoryMember>
    fun findAllByAdvertisementId(id: Long): List<InternshipCategoryMember>
}