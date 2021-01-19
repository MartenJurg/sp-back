package ee.developest.studentpraxis.model

import java.util.*
import javax.persistence.*

@Entity
@Table(schema = "sp", name = "invoice")
data class Invoice(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "invoice_id")
        val id: Long = -1,

        @Column(name = "pdf_file", nullable = false)
        var pdfFile: String,

        @Column(name = "created_timestamp", nullable = false)
        val createdTimestamp: Date = Date(),

        @ManyToOne
        @JoinColumn(name = "deal_id", referencedColumnName = "deal_id", nullable = false)
        val deal: Deal,

        @ManyToOne
        @JoinColumn(name = "status_id", referencedColumnName = "invoice_status_id", nullable = false)
        var invoiceStatus: InvoiceStatus,

        @ManyToMany(mappedBy = "invoices", fetch = FetchType.LAZY)
        val advertisements: List<Advertisement> = listOf()
)
