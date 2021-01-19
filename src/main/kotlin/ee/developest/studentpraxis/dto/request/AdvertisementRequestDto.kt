package ee.developest.studentpraxis.dto.request


class AdvertisementRequestDto (
        val companyId: Long,
        val title: String,
        val categories: List<Long>,
        var img: String?,
        val description: String,
        val deal: Long
)