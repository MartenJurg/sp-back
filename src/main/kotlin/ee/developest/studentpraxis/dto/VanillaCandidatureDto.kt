package ee.developest.studentpraxis.dto

import ee.developest.studentpraxis.model.VanillaCandidature
import java.util.*

class VanillaCandidatureDto (
        val id: Long,
        val timestamp: Date,
        var email: String,
        var CV: String?,
        var linkedin: String?
) {
    companion object {
        fun fromModel(vanillaCandidature: VanillaCandidature): VanillaCandidatureDto {
            return VanillaCandidatureDto(
                    id = vanillaCandidature.id,
                    timestamp = vanillaCandidature.timestamp,
                    email = vanillaCandidature.email,
                    CV = vanillaCandidature.CV,
                    linkedin = vanillaCandidature.linkedin
            )
        }
    }
}