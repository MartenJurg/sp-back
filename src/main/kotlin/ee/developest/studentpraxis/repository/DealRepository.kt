package ee.developest.studentpraxis.repository

import ee.developest.studentpraxis.model.Deal
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface DealRepository: JpaRepository<Deal, Long> {
    fun findAllByDealStatusId(statusId: Long): List<Deal>
}