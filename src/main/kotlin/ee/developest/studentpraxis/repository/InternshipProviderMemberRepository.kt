package ee.developest.studentpraxis.repository

import ee.developest.studentpraxis.model.member.InternshipProviderMember
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository


@Repository
interface InternshipProviderMemberRepository: JpaRepository<InternshipProviderMember, Long> {
    fun findByUserId(userId: Long): List<InternshipProviderMember>
}