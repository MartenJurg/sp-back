package ee.developest.studentpraxis.security

import ee.developest.studentpraxis.exception.UserNotFoundException
import ee.developest.studentpraxis.model.User
import ee.developest.studentpraxis.model.UserRole
import ee.developest.studentpraxis.repository.UserRepository
import ee.developest.studentpraxis.service.UserService
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service
import java.util.stream.Collectors
import java.util.stream.Stream

@Service
class AuthUserDetailsService(private val userService: UserService) : UserDetailsService {

    override fun loadUserByUsername(username: String): UserDetails {
        val user = userService.findUserByEmail(username)
        return AuthUser.fromUserAndAuthorities(user, getAuthorities(user))
    }

    private fun getAuthorities(user: User): List<SimpleGrantedAuthority> {
        return getRoles(user)
                .map(UserRole::toSpringRole)
                .map { role: String -> SimpleGrantedAuthority(role) }
                .collect(Collectors.toList<SimpleGrantedAuthority>())
    }

    private fun getRoles(user: User): Stream<UserRole> {
        return if (user.role == UserRole.ADMIN) {
            UserRole.ROLES.stream()
        } else Stream.of(user.role)
    }

}