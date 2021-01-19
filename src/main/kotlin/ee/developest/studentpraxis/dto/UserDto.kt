package ee.developest.studentpraxis.dto

import ee.developest.studentpraxis.model.User
import java.util.*

data class UserDto(
        val id: Long,
        val email: String,
        val password: String,
        val regDate: Date,
        val image: String?
) {
    companion object {
        fun fromModel(user: User): UserDto {
            return UserDto(user.id,
                    user.email,
                    user.password,
                    user.regDate,
                    user.image)
        }
    }
}