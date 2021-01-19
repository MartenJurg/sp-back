package ee.developest.studentpraxis.repository

import ee.developest.studentpraxis.model.Invoice
import ee.developest.studentpraxis.model.InvoiceStatus
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface InvoiceRepository: JpaRepository<Invoice, Long> {

    fun findAllByInvoiceStatus(invoiceStatus: InvoiceStatus): List<Invoice>
}