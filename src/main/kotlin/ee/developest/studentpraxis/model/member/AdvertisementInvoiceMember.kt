package ee.developest.studentpraxis.model.member

import ee.developest.studentpraxis.model.Advertisement
import ee.developest.studentpraxis.model.Invoice
import ee.developest.studentpraxis.model.embeddable.AdvertisementInvoiceMemberKey
import javax.persistence.*

@Entity
@Table(schema = "sp", name = "advertisement_invoice_member")
class AdvertisementInvoiceMember (

        @EmbeddedId
        val id: AdvertisementInvoiceMemberKey,

        @ManyToOne
        @MapsId("advertisementId")
        @JoinColumn(name = "advertisement_id")
        val advertisement: Advertisement,

        @ManyToOne
        @MapsId("invoiceId")
        @JoinColumn(name = "invoice_id")
        val invoice: Invoice
) {
    companion object {
        fun fromAdvertisementInvoice(advertisement: Advertisement, invoice: Invoice): AdvertisementInvoiceMember {
            return AdvertisementInvoiceMember(
                    id = AdvertisementInvoiceMemberKey(advertisement.id, invoice.id),
                    advertisement = advertisement,
                    invoice = invoice
            )
        }
    }
}