package ee.developest.studentpraxis.repository

import ee.developest.studentpraxis.model.Advertisement
import ee.developest.studentpraxis.model.InternshipProvider
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface AdvertisementRepository: JpaRepository<Advertisement, Long> {

    @Query("SELECT distinct a.advertisement_id from sp.advertisement as a " +
            "where a.start_timestamp is not null " +
            "and a.end_timestamp is not null " +
            "and a.end_timestamp > current_date ",
            nativeQuery = true)
    fun findAllActive(): List<Long>

    @Query("select distinct a.advertisement_id from sp.advertisement as a, sp.internship_category_member as icm " +
            "where icm.advertisement_id = a.advertisement_id " +
            "and icm.internship_category_id = ?1 " +
            "and a.start_timestamp IS NOT NULL " +
            "and a.end_timestamp IS NOT NULL " +
            "and a.end_timestamp > current_date",
            nativeQuery = true)
    fun findActiveByCategoryId(categoryId: Long): List<Long>

    fun findAllByInternshipProvider(internshipProvider: InternshipProvider): List<Advertisement>

    fun findAllByEndTimestampNotNullAndStartTimestampNotNull(): List<Advertisement>
    fun findDistinctByInternshipProviderIdOrderByCreatedTimestampDesc(id: Long): List<Advertisement>
    fun findAllById(id: Long): List<Advertisement>
}