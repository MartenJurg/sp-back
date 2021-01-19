package ee.developest.studentpraxis.repository

import ee.developest.studentpraxis.model.ContactType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ContactTypeRepository: JpaRepository<ContactType, Long> {
}