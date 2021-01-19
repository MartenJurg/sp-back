package ee.developest.studentpraxis.model

import javax.persistence.*

@Entity
@Table(schema = "sp", name = "deal")
class Deal (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "deal_id")
    val id: Long = -1,

    @Column(name = "cost", nullable = false)
    val cost: Int,

    @Column(name = "duration", nullable = false)
    val duration: Int,

    @ManyToOne
    @JoinColumn(name = "status_id", referencedColumnName = "deal_status_id", nullable = false)
    val dealStatus: DealStatus

)