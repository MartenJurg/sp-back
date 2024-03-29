package ee.developest.studentpraxis.repository

import ee.developest.studentpraxis.model.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface UserRepository: JpaRepository<User, Long> {
    fun findByEmail(email: String) : Optional<User>
}