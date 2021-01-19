package ee.developest.studentpraxis.model.member

import ee.developest.studentpraxis.model.InternshipProvider
import ee.developest.studentpraxis.model.User
import ee.developest.studentpraxis.model.embeddable.InternshipProviderMemberKey
import javax.persistence.*

@Entity
@Table(schema = "sp", name = "internship_provider_member")
class InternshipProviderMember(

        @EmbeddedId
        val id: InternshipProviderMemberKey,

        @ManyToOne
        @MapsId("userId")
        @JoinColumn(name = "user_id")
        val user: User,

        @ManyToOne
        @MapsId("internshipProviderId")
        @JoinColumn(name = "internship_provider_id")
        val internshipProvider: InternshipProvider
) {
        companion object {
                fun fromUserInternshipProvider(user: User, internshipProvider: InternshipProvider): InternshipProviderMember {
                        return InternshipProviderMember(
                                id = InternshipProviderMemberKey(user.id, internshipProvider.id),
                                user = user,
                                internshipProvider = internshipProvider
                        )
                }
        }
}