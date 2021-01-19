package ee.developest.studentpraxis.model

import ee.developest.studentpraxis.dto.request.InternshipProviderRequestDto
import ee.developest.studentpraxis.model.member.InternshipProviderMember
import javax.persistence.*

@Entity
@Table(schema = "sp", name = "internship_provider")
data class InternshipProvider(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "internship_provider_id")
        val id: Long = -1,

        @Column(name = "registry_code", nullable = false)
        val registryCode: Int,

        @Column(name = "company_name", nullable = false)
        val companyName: String,

        @Column(name = "introduction", nullable = true)
        val introduction: String,

        @Column(name = "image", nullable = true)
        var image: String? = null,

        @OneToMany(mappedBy = "internshipProvider")
        val users: MutableSet<InternshipProviderMember> = HashSet()
) {
        companion object {
                fun fromInternshipProviderDto(internshipProviderRequestDto: InternshipProviderRequestDto): InternshipProvider {
                        return InternshipProvider(
                                registryCode = internshipProviderRequestDto.registryCode,
                                companyName = internshipProviderRequestDto.name,
                                introduction = ""
                        )
                }
        }
}