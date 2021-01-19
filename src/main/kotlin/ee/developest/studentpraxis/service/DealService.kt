package ee.developest.studentpraxis.service

import ee.developest.studentpraxis.model.Deal
import ee.developest.studentpraxis.model.DealStatus
import ee.developest.studentpraxis.repository.DealRepository
import ee.developest.studentpraxis.repository.DealStatusRepository
import org.springframework.stereotype.Service
import java.util.*

@Service
class DealService(
        private val dealRepository: DealRepository,
        private val dealStatusRepository: DealStatusRepository) {

    fun findById(id: Long): Deal {
        return dealRepository.findById(id).orElse(null) ?: throw Exception("Deal not found")
    }

    fun saveDeal(cost: Int, duration: Int): Deal {
        return dealRepository.save(Deal(cost = cost, duration = duration, dealStatus = getInitDealStatus()))
    }

    fun updateDeal(dealId: Long, statusId: Long): Deal {
        val deal = dealRepository.findById(dealId).get()
        return dealRepository.save(Deal(
                id = deal.id,
                cost = deal.cost,
                duration = deal.duration,
                dealStatus = getDealStatusById(statusId)))
    }

    fun getAllDeals(): List<Deal> {
        return dealRepository.findAll()
    }

    fun getAllActiveDeals(): List<Deal> {
        return dealRepository.findAllByDealStatusId(2)
    }

    fun getInitDealStatus(): DealStatus {
        return dealStatusRepository.findById(1).get()
    }

    fun getActiveDealStatus(): DealStatus {
        return dealStatusRepository.findById(2).get()
    }

    fun getNotActiveDealStatus(): DealStatus {
        return dealStatusRepository.findById(3).get()
    }

    fun getDeletedDealStatus(): DealStatus {
        return dealStatusRepository.findById(4).get()
    }

    fun getDealStatusById(dealId: Long): DealStatus {
        return dealStatusRepository.findById(dealId).get()
    }

    fun getAllStatuses(): List<DealStatus> {
        return dealStatusRepository.findAll()
    }
}