package ee.developest.studentpraxis.dto.response

import ee.developest.studentpraxis.dto.InternshipCategoryDto
import ee.developest.studentpraxis.model.Advertisement
import ee.developest.studentpraxis.model.Deal
import ee.developest.studentpraxis.model.InternshipProvider
import java.util.*

class AdvertisementDto(
        val id: Long,
        val image: String?,
        val text: String?,
        val title: String?,
        val createdStamp: Date,
        val startStamp: Date?,
        val endStamp: Date?,
        var categories: List<InternshipCategoryDto>? = null,
        var internshipProvider: InternshipProviderResponseDto? = null
) {
    companion object {
        fun fromAdvertisement(ad: Advertisement): AdvertisementDto {
            return AdvertisementDto(
                    id = ad.id,
                    image = ad.image,
                    text = ad.text,
                    title = ad.title,
                    createdStamp = ad.createdTimestamp,
                    startStamp = ad.startTimestamp,
                    endStamp = ad.endTimestamp
            )
        }
    }
}