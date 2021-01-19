package ee.developest.studentpraxis.model

import java.util.*
import javax.persistence.*

@Entity
@Table(schema = "sp", name = "curriculum_vitae")
data class CurriculumVitae(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "curriculum_vitae_id")
        val id: Long = -1,

        @Column(name = "upload_time", nullable = false)
        val uploadTime: Date,

        @Column(name = "file", nullable = false)
        val file: String,

        @ManyToOne
        @JoinColumn(name = "user_id", nullable = false)
        val user: User
)
