package ee.developest.studentpraxis.dto

import ee.developest.studentpraxis.model.Deal
import ee.developest.studentpraxis.model.InvoiceStatus
import ee.developest.studentpraxis.model.Invoice
import java.util.*

class InvoiceDto (
        val id: Long,
        val pdfFile: String,
        val createdTimestamp: Date,
        val deal: Deal,
        val invoiceStatus: InvoiceStatus
) {
    companion object {
        fun fromModel(invoice: Invoice): InvoiceDto {
            return InvoiceDto(invoice.id,
                    invoice.pdfFile,
                    invoice.createdTimestamp,
                    invoice.deal,
                    invoice.invoiceStatus)
        }
    }
}