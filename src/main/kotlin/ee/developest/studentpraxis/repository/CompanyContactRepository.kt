package ee.developest.studentpraxis.repository

import ee.developest.studentpraxis.model.CompanyContact
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface CompanyContactRepository: JpaRepository<CompanyContact, Long> {

    fun findFirstByCompanyIdAndContactTypeIdOrderByCreatedTimestampDesc(id: Long, contactId: Long): Optional<CompanyContact>

    fun findFirstByCompanyIdOrderByCompanyIdDesc(id: Long): Optional<CompanyContact>

}