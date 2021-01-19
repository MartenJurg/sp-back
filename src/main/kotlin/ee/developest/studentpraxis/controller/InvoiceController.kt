package ee.developest.studentpraxis.controller

import ee.developest.studentpraxis.dto.InvoiceDto
import ee.developest.studentpraxis.model.UserRole
import ee.developest.studentpraxis.service.InvoiceService
import org.springframework.security.access.annotation.Secured
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("invoices")
class InvoiceController( private val invoiceService: InvoiceService) {

    @Secured(UserRole.Companion.SpringRole.ADMIN)
    @PatchMapping("/paid/{invoiceId}")
    fun markInvoiceAsPaid(@PathVariable invoiceId: String) {
        invoiceService.markInvoiceAsPaid(invoiceId.toLong())
    }

    @Secured(UserRole.Companion.SpringRole.ADMIN)
    @GetMapping
    fun getInvoices(): List<InvoiceDto> {
        return invoiceService.getAll()
    }

    @Secured(UserRole.Companion.SpringRole.ADMIN)
    @GetMapping("/paid")
    fun getPaidInvoices(): List<InvoiceDto> {
        return invoiceService.getPaidInvoices()
    }

    @Secured(UserRole.Companion.SpringRole.REGULAR_USER)
    @GetMapping("/by-advertisement/{advertisementId}")
    fun getInvoice(@PathVariable advertisementId: String): InvoiceDto {
        return invoiceService.getInvoiceByAdvertisementId(advertisementId.toLong())
    }

    @Secured(UserRole.Companion.SpringRole.ADMIN)
    @GetMapping("/not-paid")
    fun getNotPaidInvoices(): List<InvoiceDto> {
        return invoiceService.getNotPaidInvoices()
    }
}