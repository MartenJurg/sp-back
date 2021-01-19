package ee.developest.studentpraxis.repository

import ee.developest.studentpraxis.model.member.AdvertisementInvoiceMember
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface AdvertisementInvoiceMemberRepository: JpaRepository<AdvertisementInvoiceMember, Long> {

    fun findAllByAdvertisementId(advertisementId: Long): List<AdvertisementInvoiceMember>

    fun findAllByInvoiceId(invoiceId: Long): List<AdvertisementInvoiceMember>
}