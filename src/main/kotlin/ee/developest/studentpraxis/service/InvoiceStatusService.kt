package ee.developest.studentpraxis.service

import ee.developest.studentpraxis.model.InvoiceStatus
import ee.developest.studentpraxis.repository.InvoiceStatusRepository
import org.springframework.stereotype.Service

@Service
class InvoiceStatusService(private val invoiceStatusRepository: InvoiceStatusRepository) {
    fun getGeneratedStatus(): InvoiceStatus {
        return invoiceStatusRepository.findById(1).get()
    }

    fun getSentStatus(): InvoiceStatus {
        return invoiceStatusRepository.findById(2).get()
    }

    fun getPaidStatus(): InvoiceStatus {
        return invoiceStatusRepository.findById(3).get()
    }

    fun getExpiredStatus(): InvoiceStatus {
        return invoiceStatusRepository.findById(4).get()
    }

    fun getInvalidStatus(): InvoiceStatus {
        return invoiceStatusRepository.findById(5).get()
    }
}