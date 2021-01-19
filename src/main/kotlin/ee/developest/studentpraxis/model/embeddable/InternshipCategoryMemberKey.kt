package ee.developest.studentpraxis.model.embeddable

import java.io.Serializable
import javax.persistence.Column
import javax.persistence.Embeddable

@Embeddable
class InternshipCategoryMemberKey (
        @Column(name = "advertisement_id")
        val advertisementId: Long,

        @Column(name = "internship_category_id")
        val internshipCategoryId: Long
) : Serializable {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as InternshipCategoryMemberKey

        if (advertisementId != other.advertisementId) return false
        if (internshipCategoryId != other.internshipCategoryId) return false

        return true
    }

    override fun hashCode(): Int {
        var result = advertisementId.hashCode()
        result = 31 * result + internshipCategoryId.hashCode()
        return result
    }
}