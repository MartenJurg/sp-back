package ee.developest.studentpraxis.model

import java.util.*
import javax.persistence.*

@Entity
@Table(schema = "sp", name = "user_contact")
data class UserContact (
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "contact_id")
        val id: Long = -1,

        @Column(name = "value", nullable = false)
        val value: String,

        @Column(name = "created_timestamp", nullable = false)
        val createdTimestamp: Date,

        @ManyToOne
        @JoinColumn(name = "user_id", nullable = false)
        val user: User,

        @ManyToOne
        @JoinColumn(name = "contact_type_id", referencedColumnName = "contact_type_id", nullable = false)
        val contactType: ContactType
)