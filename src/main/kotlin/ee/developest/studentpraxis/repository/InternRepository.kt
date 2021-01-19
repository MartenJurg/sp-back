package ee.developest.studentpraxis.repository

import ee.developest.studentpraxis.model.Intern
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository


@Repository
interface InternRepository: JpaRepository<Intern, Long> {
}