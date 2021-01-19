package ee.developest.studentpraxis.model

import ee.developest.studentpraxis.dto.request.AdvertisementRequestDto
import java.util.*
import javax.persistence.*
import kotlin.collections.HashSet

@Entity
@Table(schema = "sp", name = "advertisement")
data class Advertisement(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "advertisement_id")
        val id: Long = -1,

        @Column(name = "image", nullable = true)
        var image: String?,

        @Column(name = "text", nullable = true)
        val text: String?,

        @Column(name = "internship_title", nullable = false)
        val title: String,

        @Column(name = "created_timestamp", nullable = false)
        val createdTimestamp: Date = Date(),

        @Column(name = "start_timestamp", nullable = true)
        var startTimestamp: Date? = null,

        @Column(name = "end_timestamp", nullable = true)
        var endTimestamp: Date? = null,

        @ManyToOne
        @JoinColumn(name = "internship_provider_id", referencedColumnName = "internship_provider_id", nullable = false)
        val internshipProvider: InternshipProvider,

        @ManyToMany(fetch = FetchType.LAZY)
        @JoinTable(name = "advertisement_invoice_member",
                joinColumns = [JoinColumn(name = "advertisement_id", referencedColumnName = "advertisement_id")],
                inverseJoinColumns = [JoinColumn(name = "invoice_id", referencedColumnName = "invoice_id")])
        val invoices: Set<Invoice> = HashSet(),

        @ManyToMany(fetch = FetchType.LAZY)
        @JoinTable(name = "internship_category_member",
                joinColumns = [JoinColumn(name = "advertisement_id", referencedColumnName = "advertisement_id")],
                inverseJoinColumns = [JoinColumn(name = "internship_category_id", referencedColumnName = "internship_category_id")])
        val internshipCategories: Set<InternshipCategory> = HashSet()
) {
        companion object {
                fun fromAdvertisementDto(
                        adDto: AdvertisementRequestDto,
                        internshipProvider: InternshipProvider
                ): Advertisement {
                        return Advertisement(
                                title = adDto.title,
                                image = null,
                                text = adDto.description,
                                internshipProvider = internshipProvider)
                }
        }
}
