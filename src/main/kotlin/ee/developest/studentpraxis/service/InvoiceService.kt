package ee.developest.studentpraxis.service

import ee.developest.studentpraxis.dto.InvoiceDto
import ee.developest.studentpraxis.model.Advertisement
import ee.developest.studentpraxis.model.Deal
import ee.developest.studentpraxis.model.Invoice
import ee.developest.studentpraxis.repository.AdvertisementRepository
import ee.developest.studentpraxis.repository.InvoiceRepository
import org.springframework.stereotype.Service
import java.awt.LinearGradientPaint
import java.lang.Exception
import java.util.*

@Service
class InvoiceService(
        private val invoiceRepository: InvoiceRepository,
        private val invoiceStatusService: InvoiceStatusService,
        private val advertisementInvoiceMemberService: AdvertisementInvoiceMemberService,
        private val advertisementRepository: AdvertisementRepository,
        private val dealService: DealService,
        private val fileService: FileService
) {

    fun getInvoiceByAdvertisementId(advertisementId: Long): InvoiceDto {
        val adInvoiceMemberList = advertisementInvoiceMemberService.findAllByAdvertisementId(advertisementId)
        if (adInvoiceMemberList.size == 0) throw Exception("Invoice not found!")
        val latestInvoice = adInvoiceMemberList.maxBy { it.invoice.createdTimestamp }
        return InvoiceDto.fromModel(latestInvoice!!.invoice)
    }

    fun getNotPaidInvoices(): List<InvoiceDto> {
        val sent = invoiceRepository.findAllByInvoiceStatus(invoiceStatusService.getSentStatus())
        val generated = invoiceRepository.findAllByInvoiceStatus(invoiceStatusService.getGeneratedStatus())
        val invoices = sent + generated
        return invoices
                .map { invoice -> InvoiceDto.fromModel(invoice) }
    }

    fun getPaidInvoices(): List<InvoiceDto> {
        val paid = invoiceRepository.findAllByInvoiceStatus(invoiceStatusService.getPaidStatus())
        val expired = invoiceRepository.findAllByInvoiceStatus(invoiceStatusService.getExpiredStatus())
        val invalid = invoiceRepository.findAllByInvoiceStatus(invoiceStatusService.getInvalidStatus())
        val invoices = paid + expired + invalid
        return invoices
                .map { invoice -> InvoiceDto.fromModel(invoice) }
    }

    fun getAll(): List<InvoiceDto> {
        return invoiceRepository.findAll()
                .map { invoice -> InvoiceDto.fromModel(invoice) }
    }

    fun findById(invoiceId: Long): Invoice {
        return invoiceRepository.findById(invoiceId).orElse(null) ?: throw Exception("Invoice not found")
    }

    fun markInvoiceAsPaid(invoiceId: Long) {
        val invoice = findById(invoiceId)
        invoice.invoiceStatus = invoiceStatusService.getPaidStatus()
        invoiceRepository.save(invoice);

        val advertisementInvoiceMemberList = advertisementInvoiceMemberService.findAllByInvoiceId(invoice.id)
        for ( advertisementInvoiceMember in advertisementInvoiceMemberList) {
            val advertisement = advertisementInvoiceMember.advertisement
            advertisement.startTimestamp = Date()
            advertisement.endTimestamp = Date(Date().time + (invoice.deal.duration.toLong() * 24 * 60 * 60 * 1000))
            advertisementRepository.save(advertisement)
        }
    }

    fun generateInvoice(companyId: Long, dealId: Long, advertisements: List<Advertisement>): Invoice {
        val invoice = invoiceRepository.save(Invoice(
                pdfFile = "",
                invoiceStatus = invoiceStatusService.getGeneratedStatus(),
                advertisements = advertisements,
                deal = dealService.findById(dealId)
        ))

        invoice.pdfFile = fileService.generatePdfForInvoice(invoice.id, companyId, dealId)

        return invoiceRepository.save(invoice)
    }
}