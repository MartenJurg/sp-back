package ee.developest.studentpraxis.dto.response

import ee.developest.studentpraxis.dto.CompanyContactDto
import ee.developest.studentpraxis.model.CompanyContact
import ee.developest.studentpraxis.model.InternshipProvider

data class InternshipProviderResponseDto(
        val id: Long?,
        val name: String?,
        val registryCode: Int?,
        val email: String?,
        val phone: String?,
        val image: String? = null,
        val address: String?
) {
    companion object {
        fun fromInternshipProvider(internshipProvider: InternshipProvider, email: CompanyContact?, phone: CompanyContact?, address: CompanyContact?): InternshipProviderResponseDto {
            return InternshipProviderResponseDto(
                    id = internshipProvider.id,
                    name = internshipProvider.companyName,
                    registryCode = internshipProvider.registryCode,
                    email = email?.value,
                    phone = phone?.value,
                    image = internshipProvider.image,
                    address = address?.value
            )
        }

        fun empty(): InternshipProviderResponseDto {
            return InternshipProviderResponseDto(
                    id = null,
                    name = null,
                    registryCode = null,
                    email = null,
                    phone = null,
                    address = null
            )
        }
    }
}