package ee.developest.studentpraxis.service

import ee.developest.studentpraxis.model.InternshipProvider
import ee.developest.studentpraxis.model.User
import ee.developest.studentpraxis.model.member.InternshipProviderMember
import ee.developest.studentpraxis.repository.InternshipProviderMemberRepository
import org.springframework.stereotype.Service

@Service
class InternshipProviderMemberService(private val internshipProviderMemberRepository: InternshipProviderMemberRepository) {

    fun addUserToInternshipProvider(user: User, internshipProvider: InternshipProvider): InternshipProviderMember {
        return internshipProviderMemberRepository.save(InternshipProviderMember.fromUserInternshipProvider(user, internshipProvider))
    }

    fun findAllByUserId(userId: Long): List<InternshipProviderMember> {
        return internshipProviderMemberRepository.findByUserId(userId)
    }
}