package ee.developest.studentpraxis.dto

import ee.developest.studentpraxis.model.CompanyContact
import ee.developest.studentpraxis.model.ContactType

class CompanyContactDto(val value: String,
                        val type: ContactType) {
    companion object {
        fun fromCompanyContact(contact: CompanyContact): CompanyContactDto {
            return CompanyContactDto(
                    value = contact.value,
                    type = contact.contactType)
        }
    }
}