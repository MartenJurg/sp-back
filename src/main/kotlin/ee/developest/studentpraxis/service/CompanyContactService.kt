package ee.developest.studentpraxis.service

import ee.developest.studentpraxis.model.CompanyContact
import ee.developest.studentpraxis.model.ContactType
import ee.developest.studentpraxis.model.InternshipProvider
import ee.developest.studentpraxis.model.User
import ee.developest.studentpraxis.repository.CompanyContactRepository
import org.springframework.stereotype.Service
import java.util.*

@Service
class CompanyContactService(private val companyContactRepository: CompanyContactRepository) {

    fun save(internshipProvider: InternshipProvider, type: ContactType, value: String): CompanyContact {
        return companyContactRepository.save(CompanyContact(
                value = value,
                contactType = type,
                company = internshipProvider,
                createdTimestamp = Date()))
    }

    fun getByCompanyIdAndType(companyId: Long, type: ContactType): Optional<CompanyContact> {
        return companyContactRepository.findFirstByCompanyIdAndContactTypeIdOrderByCreatedTimestampDesc(companyId, type.id)
    }

    fun getByCompanyId(companyId: Long): Optional<CompanyContact> {
        return companyContactRepository.findFirstByCompanyIdOrderByCompanyIdDesc(companyId)
    }
}