package ee.developest.studentpraxis.model.embeddable

import java.io.Serializable
import javax.persistence.Column
import javax.persistence.Embeddable

@Embeddable
class AdvertisementInvoiceMemberKey (
        @Column(name = "advertisement_id")
        val advertisementId: Long,

        @Column(name = "invoice_id")
        val invoiceId: Long
) : Serializable {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as AdvertisementInvoiceMemberKey

        if (advertisementId != other.advertisementId) return false
        if (invoiceId != other.invoiceId) return false

        return true
    }

    override fun hashCode(): Int {
        var result = advertisementId.hashCode()
        result = 31 * result + invoiceId.hashCode()
        return result
    }
}