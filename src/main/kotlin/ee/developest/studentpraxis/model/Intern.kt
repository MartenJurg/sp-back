package ee.developest.studentpraxis.model

import ee.developest.studentpraxis.dto.request.AuthRequestDto
import org.springframework.security.crypto.password.PasswordEncoder
import java.util.*
import javax.persistence.*

@Entity
@Table(schema = "sp", name = "intern")
data class Intern (
        @Id
        @Column(name = "user_id")
        val id: Long = -1,

        @Column(name = "full_name", nullable = false)
        var full_name: String,

        @Column(name = "about", nullable = true)
        var about: String? = null,

        @OneToOne(cascade = [CascadeType.ALL])
        @JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = false)
        val user: User
) {
        companion object {
                fun baseIntern(user: User): Intern {
                        return Intern(id = user.id, full_name = "", user = user)
                }
        }
}