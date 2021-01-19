package ee.developest.studentpraxis.model

import ee.developest.studentpraxis.dto.UserRoleDto
import javax.persistence.*

@Entity
@Table(schema = "sp", name = "user_role")
data class UserRole(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "role_id")
        val id: Long = -1,

        @Column(name = "role", nullable = false)
        val name: String


) {
    companion object {
        class SpringRole {
            companion object {
                const val REGULAR_USER = "ROLE_REGULAR_USER"
                const val ADMIN = "ROLE_ADMIN"
            }
        }

        val REGULAR_USER = UserRole(1, SpringRole.REGULAR_USER)
        val ADMIN = UserRole(2, SpringRole.ADMIN)
        val ROLES = listOf(REGULAR_USER, ADMIN)
    }

    fun toSpringRole(): String {
        return name
    }

    fun toUserRoleDto(): UserRoleDto {
        return UserRoleDto(id = id, role = name)
    }
}