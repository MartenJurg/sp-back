package ee.developest.studentpraxis.controller

import ee.developest.studentpraxis.dto.request.AdvertisementRequestDto
import ee.developest.studentpraxis.dto.response.AdvertisementDto
import ee.developest.studentpraxis.dto.response.AdvertisementResponseDto
import ee.developest.studentpraxis.model.Advertisement
import ee.developest.studentpraxis.model.UserRole
import ee.developest.studentpraxis.security.JwtTokenProvider
import ee.developest.studentpraxis.service.AdvertisementService
import ee.developest.studentpraxis.service.FileService
import org.springframework.security.access.annotation.Secured
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import javax.annotation.Resource

@RestController
@RequestMapping("advertisements")
class AdvertisementController(
        private val advertisementService: AdvertisementService,
        private val jwtTokenProvider: JwtTokenProvider,
        private val fileService: FileService) {


    @Secured(UserRole.Companion.SpringRole.ADMIN, UserRole.Companion.SpringRole.REGULAR_USER)
    @GetMapping("/user")
    fun getAdvertisementsByInternshipProviderId(@RequestHeader(name="Authorization") token: String): List<AdvertisementDto> {
        return advertisementService.getAdvertisementsByUserId(jwtTokenProvider.getUserIdFromToken(jwtTokenProvider.removeBearerFromToken(token))!!)
    }

    @GetMapping("/active")
    fun getAllActiveAdvertisements(): List<AdvertisementDto> {
        // TODO
        return advertisementService.getAllActiveAdvertisements();
    }

    @GetMapping
    fun getAdvertisementsByCategories(@RequestParam categories: List<Long>): List<AdvertisementDto> {
        return advertisementService.getAdvertisementsByCategories(categories);
    }

    @Secured(UserRole.Companion.SpringRole.ADMIN, UserRole.Companion.SpringRole.REGULAR_USER)
    @PostMapping
    fun saveAdvertisement(
            @RequestParam("companyId") companyId: Long,
            @RequestParam("title") title: String,
            @RequestParam("categories") categories: List<Long>,
            @RequestParam("img") img: MultipartFile?,
            @RequestParam("description") description: String,
            @RequestParam("deal") deal: Long): AdvertisementDto {

        return advertisementService.saveAdvertisement(AdvertisementRequestDto(companyId, title, categories, null, description, deal), img)
    }

//    @Secured(UserRole.Companion.SpringRole.ADMIN, UserRole.Companion.SpringRole.REGULAR_USER)
//    @PostMapping
//    fun saveAdvertisement(
//            @RequestBody advertisementRequestDto: AdvertisementRequestDto,
//            @RequestHeader(name="Authorization") token: String): AdvertisementDto {
//
//        return advertisementService.saveAdvertisement(advertisementRequestDto)
//    }
//
//    @Secured(UserRole.Companion.SpringRole.ADMIN, UserRole.Companion.SpringRole.REGULAR_USER)
//    @PostMapping("/picture")
//    fun saveAdvertisementImg(
//            @RequestParam("img") img: MultipartFile,
//            @RequestParam("advertisementId") advertisementId: Long): String? {
//        return fileService.saveAdvertisementPicture(advertisementService.findModelById(advertisementId), img)
//    }
}