package ee.developest.studentpraxis.model

import javax.persistence.*

@Entity
@Table(schema = "sp", name = "invoice_status")
data class InvoiceStatus (
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "invoice_status_id")
        val id: Long = -1,

        @Column(name = "status", nullable = false)
        val name: String
){
    companion object {
        val GENERATED = InvoiceStatus(1, "GENERATED")
        val SENT = InvoiceStatus(2, "SENT")
        val PAID = InvoiceStatus(3, "PAID")
        val EXPIRED = InvoiceStatus(4, "EXPIRED")
        val INVALID = InvoiceStatus(5, "INVALID")
    }
}