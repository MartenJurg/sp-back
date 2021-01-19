package ee.developest.studentpraxis.controller

import ee.developest.studentpraxis.dto.request.ProfileRequestDto
import ee.developest.studentpraxis.dto.response.ProfileResponseDto
import ee.developest.studentpraxis.model.CurriculumVitae
import ee.developest.studentpraxis.model.UserRole
import ee.developest.studentpraxis.security.JwtTokenProvider
import ee.developest.studentpraxis.service.FileService
import ee.developest.studentpraxis.service.InternService
import ee.developest.studentpraxis.service.ProfileService
import org.springframework.security.access.annotation.Secured
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.io.InputStream


@RestController
@RequestMapping("profile")
class ProfileController(
        private val jwtTokenProvider: JwtTokenProvider,
        private val internService: InternService,
        private val profileService: ProfileService,
        private val fileService: FileService
) {

    @Secured(UserRole.Companion.SpringRole.ADMIN, UserRole.Companion.SpringRole.REGULAR_USER)
    @PutMapping("/personal")
    fun update(
            @RequestBody profileRequestDto: ProfileRequestDto,
            @RequestHeader(name = "Authorization") token: String
    ) {
        profileService.updatePersonalData(jwtTokenProvider.getUserIdFromToken(jwtTokenProvider.removeBearerFromToken(token))!!, profileRequestDto)
    }

    @Secured(UserRole.Companion.SpringRole.ADMIN, UserRole.Companion.SpringRole.REGULAR_USER)
    @GetMapping("/personal")
    fun getPersonal(
            @RequestHeader(name = "Authorization") token: String
    ): ProfileResponseDto {
        return profileService.getPersonalData(jwtTokenProvider.getUserIdFromToken(jwtTokenProvider.removeBearerFromToken(token))!!)
    }

    @Secured(UserRole.Companion.SpringRole.ADMIN, UserRole.Companion.SpringRole.REGULAR_USER)
    @PutMapping("/cv")
    fun addCv(
            @RequestParam("cv") cv: MultipartFile,
            @RequestHeader(name = "Authorization") token: String
    ): String? {
        return fileService.saveCV(jwtTokenProvider.getUserIdFromToken(jwtTokenProvider.removeBearerFromToken(token))!!, cv)
    }

    @Secured(UserRole.Companion.SpringRole.ADMIN, UserRole.Companion.SpringRole.REGULAR_USER)
    @GetMapping("/cv")
    fun getCv(
            @RequestHeader(name = "Authorization") token: String
    ): String? {
        return fileService.getLastCvByUserId(jwtTokenProvider.getUserIdFromToken(jwtTokenProvider.removeBearerFromToken(token))!!)
    }

    @Secured(UserRole.Companion.SpringRole.ADMIN, UserRole.Companion.SpringRole.REGULAR_USER)
    @PutMapping("/picture")
    fun addProfilePicture(
            @RequestParam("profilePicture") profilePicture: MultipartFile,
            @RequestHeader(name = "Authorization") token: String
    ): String? {
        return fileService.saveInternProfilePicture(jwtTokenProvider.getUserIdFromToken(jwtTokenProvider.removeBearerFromToken(token))!!, profilePicture)
    }

    @Secured(UserRole.Companion.SpringRole.ADMIN, UserRole.Companion.SpringRole.REGULAR_USER)
    @GetMapping("/picture")
    fun getProfilePicture(
            @RequestHeader(name = "Authorization") token: String
    ): String? {
        return fileService.getLastInternProfilePicture(jwtTokenProvider.getUserIdFromToken(jwtTokenProvider.removeBearerFromToken(token))!!)
    }
//
//    @Secured(UserRole.Companion.SpringRole.ADMIN, UserRole.Companion.SpringRole.REGULAR_USER)
//    @GetMapping("/all")
//    fun getAll(): List<InternsipProviderResponseDto> {
//        return internshipProviderService.getAll()
//                .map { company -> InternsipProviderResponseDto.fromInternshipProvider(company) }
//    }
}