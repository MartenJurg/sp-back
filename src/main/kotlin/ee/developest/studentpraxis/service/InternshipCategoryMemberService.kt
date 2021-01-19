package ee.developest.studentpraxis.service

import ee.developest.studentpraxis.model.Advertisement
import ee.developest.studentpraxis.model.InternshipCategory
import ee.developest.studentpraxis.model.member.InternshipCategoryMember
import ee.developest.studentpraxis.repository.InternshipCategoryMemberRepository
import org.springframework.stereotype.Service

@Service
class InternshipCategoryMemberService(
        private val internshipCategoryMemberRepository: InternshipCategoryMemberRepository
) {

    fun save(advertisement: Advertisement, internshipCategory: InternshipCategory): InternshipCategoryMember {
        return internshipCategoryMemberRepository.save(InternshipCategoryMember
                .fromAdvertisementInternshipCategory(advertisement, internshipCategory))
    }

    fun findAllByAdvertisementId(advertisementId: Long): List<InternshipCategoryMember> {
        return internshipCategoryMemberRepository.findAllByAdvertisementId(advertisementId)
    }

    fun findAllByCategoryId(id: Long): List<InternshipCategoryMember> {
        return internshipCategoryMemberRepository.findAllByInternshipCategoryId(id)
    }

    fun findAllByCategoryIds(ids: List<Long>): List<InternshipCategoryMember> {
        val list = ArrayList<InternshipCategoryMember>()
        for (id in ids) {
            list.addAll(findAllByCategoryId(id))
        }
        return list
    }
}