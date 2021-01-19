package ee.developest.studentpraxis.service

import ee.developest.studentpraxis.model.Advertisement
import ee.developest.studentpraxis.model.Invoice
import ee.developest.studentpraxis.model.member.AdvertisementInvoiceMember
import ee.developest.studentpraxis.repository.AdvertisementInvoiceMemberRepository
import org.springframework.stereotype.Service

@Service
class AdvertisementInvoiceMemberService(
        private val advertisementInvoiceMemberRepository: AdvertisementInvoiceMemberRepository
) {

    fun save(advertisement: Advertisement, invoice: Invoice): AdvertisementInvoiceMember {
        return advertisementInvoiceMemberRepository.save(AdvertisementInvoiceMember.fromAdvertisementInvoice(advertisement, invoice))
    }

    fun findAllByInvoiceId(invoiceId: Long): List<AdvertisementInvoiceMember> {
        return advertisementInvoiceMemberRepository.findAllByInvoiceId(invoiceId)
    }

    fun findAllByAdvertisementId(advertisementId: Long): List<AdvertisementInvoiceMember> {
        return advertisementInvoiceMemberRepository.findAllByAdvertisementId(advertisementId)
    }

}