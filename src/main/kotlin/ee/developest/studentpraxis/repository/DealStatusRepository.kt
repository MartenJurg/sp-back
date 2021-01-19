package ee.developest.studentpraxis.repository

import ee.developest.studentpraxis.model.DealStatus
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface DealStatusRepository: JpaRepository<DealStatus, Long> {
}