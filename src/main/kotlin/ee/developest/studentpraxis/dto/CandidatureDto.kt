package ee.developest.studentpraxis.dto

import ee.developest.studentpraxis.model.Candidature
import ee.developest.studentpraxis.model.Invoice
import ee.developest.studentpraxis.model.UserContact
import java.util.*

class CandidatureDto (
        val id: Long,
        val timestamp: Date,
        val internDto: InternDto,
        val linkedin: String?,
        val cvUrl: String?,
        val img: String?
        ) {
    companion object {
        fun fromModelAndContact(candidature: Candidature, cvUrl: String?, linkedin: String?): CandidatureDto {
            return CandidatureDto(
                    id = candidature.id,
                    timestamp = candidature.timestamp,
                    internDto = InternDto.fromModel(candidature.intern),
                    linkedin = linkedin,
                    cvUrl = cvUrl,
                    img = candidature.intern.user.image
            )
        }
    }
}