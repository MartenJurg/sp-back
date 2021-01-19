package ee.developest.studentpraxis.repository

import ee.developest.studentpraxis.model.CompanyContact
import ee.developest.studentpraxis.model.UserContact
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface ContactRepository: JpaRepository<UserContact, Long> {
    fun findFirstByUserIdAndContactTypeIdOrderByCreatedTimestampDesc(userId: Long, contactTypeId: Long): UserContact?
}