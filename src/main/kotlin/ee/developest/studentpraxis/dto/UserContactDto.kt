package ee.developest.studentpraxis.dto

import ee.developest.studentpraxis.model.Candidature
import ee.developest.studentpraxis.model.UserContact
import java.util.*

class UserContactDto (
        val id: Long,
        val value: String,
        val createdTimestamp: Date,
        val type: String
) {
    companion object {
        fun fromModel(userContact: UserContact): UserContactDto {
            return UserContactDto(
                    id = userContact.id,
                    value = userContact.value,
                    createdTimestamp = userContact.createdTimestamp,
                    type = userContact.contactType.name
            )
        }
    }
}