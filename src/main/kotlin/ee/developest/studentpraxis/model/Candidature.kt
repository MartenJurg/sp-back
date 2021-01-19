package ee.developest.studentpraxis.model

import java.util.*
import javax.persistence.*

@Entity
@Table(schema = "sp", name = "candidature")
data class Candidature (
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "candidature_id")
        val id: Long = -1,

        @Column(name = "timestamp", nullable = false)
        val timestamp: Date,

        @Column(name = "message", nullable = true)
        var message: String? = null,

        @Column(name = "active", nullable = false)
        var active: Boolean = true,

        @ManyToOne
        @JoinColumn(name = "intern_id", referencedColumnName = "user_id", nullable = false)
        val intern: Intern,

        @ManyToOne
        @JoinColumn(name = "advertisement_id", referencedColumnName = "advertisement_id", nullable = false)
        val advertisement: Advertisement
)