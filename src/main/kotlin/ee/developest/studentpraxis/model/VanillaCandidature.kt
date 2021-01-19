package ee.developest.studentpraxis.model

import java.util.*
import javax.persistence.*

@Entity
@Table(schema = "sp", name = "vanilla_candidature")
data class VanillaCandidature (
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "vanilla_candidature_id")
        val id: Long = -1,

        @Column(name = "timestamp", nullable = false)
        val timestamp: Date,

        @Column(name = "email", nullable = false)
        var email: String,

        @Column(name = "curriculum_vitae", nullable = true)
        var CV: String? = null,

        @Column(name = "linkedin", nullable = true)
        var linkedin: String? = null,

        @ManyToOne
        @JoinColumn(name = "advertisement_id", referencedColumnName = "advertisement_id", nullable = false)
        val advertisement: Advertisement
)