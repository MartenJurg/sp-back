package ee.developest.studentpraxis.dto

import ee.developest.studentpraxis.model.Intern

class InternDto (
        val id: Long,
        val full_name: String,
        val about: String?,
        val email: String
) {
    companion object {
        fun fromModel(intern: Intern): InternDto {
            return InternDto(
                    id = intern.id,
                    full_name = intern.full_name,
                    about = intern.about,
                    email = intern.user.email
            )
        }
    }
}