package ee.developest.studentpraxis.service

import ee.developest.studentpraxis.model.User
import ee.developest.studentpraxis.repository.UserRepository
import org.springframework.stereotype.Service

@Service
class UserService(private val userRepository: UserRepository) {

    fun getAll(): List<User> {
        return userRepository.findAll()
    }

    fun saveUser(user :User): User {
        return userRepository.save(user);
    }

    fun findUserById(id: Long): User {
        val user = userRepository.findById(id)
        return user.orElse(null) ?: throw Exception("User not found")
    }

    fun findUserByEmail(email: String): User {
        return userRepository.findByEmail(email).orElse(null) ?: throw Exception("User not found")
    }

    fun isValidEmail(email: String): Boolean {
        return email.contains("@")
    }

    fun isValidPassword(password: String): Boolean {
        return PASSWORD_REGEX.matches(password)
    }

    companion object {
        val PASSWORD_REGEX = Regex("^(?=.*[A-Za-z])(?=.*[0-9])[A-Za-z0-9]{8,}$")
    }
}