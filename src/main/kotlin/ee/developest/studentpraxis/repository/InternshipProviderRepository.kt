package ee.developest.studentpraxis.repository

import ee.developest.studentpraxis.model.InternshipProvider
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface InternshipProviderRepository: JpaRepository<InternshipProvider, Long> {
}