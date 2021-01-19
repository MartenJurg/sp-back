package ee.developest.studentpraxis.model.member

import ee.developest.studentpraxis.model.Advertisement
import ee.developest.studentpraxis.model.InternshipCategory
import ee.developest.studentpraxis.model.Invoice
import ee.developest.studentpraxis.model.embeddable.AdvertisementInvoiceMemberKey
import ee.developest.studentpraxis.model.embeddable.InternshipCategoryMemberKey
import javax.persistence.*

@Entity
@Table(schema = "sp", name = "internship_category_member")
class InternshipCategoryMember (

        @EmbeddedId
        val id: InternshipCategoryMemberKey,

        @ManyToOne(fetch = FetchType.LAZY)
        @MapsId("advertisementId")
        @JoinColumn(name = "advertisement_id")
        val advertisement: Advertisement,

        @ManyToOne(fetch = FetchType.LAZY)
        @MapsId("internshipCategoryId")
        @JoinColumn(name = "internship_category_id")
        val internshipCategory: InternshipCategory
) {
    companion object {
        fun fromAdvertisementInternshipCategory(advertisement: Advertisement, internshipCategory: InternshipCategory): InternshipCategoryMember {
            return InternshipCategoryMember(
                    id = InternshipCategoryMemberKey(advertisement.id, internshipCategory.id),
                    advertisement = advertisement,
                    internshipCategory = internshipCategory
            )
        }
    }
}