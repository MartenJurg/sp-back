package ee.developest.studentpraxis.model.embeddable

import java.io.Serializable
import javax.persistence.Column
import javax.persistence.Embeddable

@Embeddable
class InternshipProviderMemberKey(
        @Column(name = "user_id")
        val userId: Long,

        @Column(name = "internship_provider_id")
        val internshipProviderId: Long
) : Serializable {
        override fun equals(other: Any?): Boolean {
                if (this === other) return true
                if (javaClass != other?.javaClass) return false

                other as InternshipProviderMemberKey

                if (userId != other.userId) return false
                if (internshipProviderId != other.internshipProviderId) return false

                return true
        }

        override fun hashCode(): Int {
                var result = userId.hashCode()
                result = 31 * result + internshipProviderId.hashCode()
                return result
        }
}