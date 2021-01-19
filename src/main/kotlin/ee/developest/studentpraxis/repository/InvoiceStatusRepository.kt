package ee.developest.studentpraxis.repository

import ee.developest.studentpraxis.model.InvoiceStatus
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface InvoiceStatusRepository: JpaRepository<InvoiceStatus, Long> {
}