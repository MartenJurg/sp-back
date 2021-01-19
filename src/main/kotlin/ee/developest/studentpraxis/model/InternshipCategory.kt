package ee.developest.studentpraxis.model

import javax.persistence.*

@Entity
@Table(schema = "sp", name = "internship_category")
data class InternshipCategory(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "internship_category_id")
        val id: Long = -1,

        @Column(name = "category", nullable = false)
        val name: String,

        @ManyToMany(mappedBy = "internshipCategories", fetch = FetchType.LAZY)
        val companies: Set<Advertisement> = HashSet()
)