package ee.developest.studentpraxis.model

import ee.developest.studentpraxis.dto.request.AuthRequestDto
import ee.developest.studentpraxis.model.member.InternshipProviderMember
import org.springframework.security.crypto.password.PasswordEncoder
import java.util.*
import javax.persistence.*
import kotlin.collections.HashSet

@Entity
@Table(schema = "sp", name = "user")
data class User(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "user_id")
        val id: Long = -1,

        @Column(name = "email", nullable = false)
        val email: String,

        @Column(name = "password", nullable = false)
        val password: String,

        @Column(name = "reg_date", nullable = false)
        val regDate: Date = Date(),

        @Column(name = "image", nullable = true)
        var image: String? = null,

        @ManyToOne
        @JoinColumn(name = "role_id", referencedColumnName = "role_id", nullable = false)
        val role: UserRole = UserRole.REGULAR_USER,

        @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
        var internshipProviders: MutableSet<InternshipProviderMember> = HashSet()
) {
    companion object {
        fun fromAuthRequestDto(registerRequestDto: AuthRequestDto, passwordEncoder: PasswordEncoder): User {
            return User(email = registerRequestDto.email,
                    password = passwordEncoder.encode(registerRequestDto.password),
                    regDate = Date(),
                    role = UserRole.REGULAR_USER)
        }
    }
}
