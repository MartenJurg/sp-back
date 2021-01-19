package ee.developest.studentpraxis.security

import ee.developest.studentpraxis.model.UserRole
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.User


class AuthUser : User {
    private var id: Long? = null
    private var role: UserRole? = null

    constructor(username: String, password: String, authorities: Collection<GrantedAuthority>, role: UserRole, id: Long) : super(username, password, authorities) {
        this.role = role
        this.id = id
    }

    constructor(username: String,
                password: String,
                enabled: Boolean,
                accountNonExpired: Boolean,
                credentialsNonExpired: Boolean,
                accountNonLocked: Boolean,
                authorities: Collection<GrantedAuthority>) : super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities)

    companion object {
        fun fromUserAndAuthorities(user: ee.developest.studentpraxis.model.User, authorities: Collection<GrantedAuthority>): AuthUser {
            return AuthUser(
                    username = user.email,
                    password = user.password,
                    authorities = authorities,
                    role = user.role,
                    id = user.id
            )
        }
    }
}