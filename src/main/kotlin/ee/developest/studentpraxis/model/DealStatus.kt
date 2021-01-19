package ee.developest.studentpraxis.model

import javax.persistence.*

@Entity
@Table(schema = "sp", name = "deal_status")
class DealStatus(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "deal_status_id")
        val id: Long = -1,

        @Column(name = "status", nullable = false)
        val name: String
)
