package ee.developest.studentpraxis.controller

import ee.developest.studentpraxis.dto.request.InternshipProviderRequestDto
import ee.developest.studentpraxis.dto.response.InternshipProviderResponseDto
import ee.developest.studentpraxis.model.UserRole
import ee.developest.studentpraxis.security.JwtTokenProvider
import ee.developest.studentpraxis.service.FileService
import ee.developest.studentpraxis.service.InternshipProviderMemberService
import ee.developest.studentpraxis.service.InternshipProviderService
import ee.developest.studentpraxis.service.UserService
import org.springframework.security.access.annotation.Secured
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("internship_provider")
class InternshipProviderController(private val internshipProviderService: InternshipProviderService,
                                   private val jwtTokenProvider: JwtTokenProvider,
                                   private val fileService: FileService) {

    @Secured(UserRole.Companion.SpringRole.ADMIN, UserRole.Companion.SpringRole.REGULAR_USER)
    @PostMapping
    fun add(
            @RequestParam("registryCode") registryCode: Int,
            @RequestParam("name") name: String,
            @RequestParam("email") email: String,
            @RequestParam("phone") phone: String,
            @RequestParam("address") address: String,
            @RequestParam("internshipProviderPicture") internshipProviderPicture: MultipartFile?,
            @RequestHeader (name="Authorization") token: String
    ): InternshipProviderResponseDto {
        return internshipProviderService.save(
                jwtTokenProvider.getUserIdFromToken(jwtTokenProvider.removeBearerFromToken(token))!!,
                InternshipProviderRequestDto(registryCode, name, email, phone, address),
                internshipProviderPicture)
    }

    @Secured(UserRole.Companion.SpringRole.ADMIN, UserRole.Companion.SpringRole.REGULAR_USER)
    @GetMapping
    fun getAllInternshipProvidersByUserId(@RequestHeader (name="Authorization") token: String): List<InternshipProviderResponseDto> {
        return internshipProviderService.findAllByUserId(jwtTokenProvider.getUserIdFromToken(jwtTokenProvider.removeBearerFromToken(token))!!)
    }

    @Secured(UserRole.Companion.SpringRole.ADMIN, UserRole.Companion.SpringRole.REGULAR_USER)
    @GetMapping("/picture")
    fun getInternshipProviderImage(
            @RequestParam internshipProviderId: Long): String? {
        return internshipProviderService.getLastInternshipProviderPicture(internshipProviderId)
    }

    @Secured(UserRole.Companion.SpringRole.ADMIN, UserRole.Companion.SpringRole.REGULAR_USER)
    @PostMapping("/picture")
    fun addInternshipProviderImage(
            @RequestParam("internshipProviderPicture") internshipProviderPicture: MultipartFile,
            @RequestParam("internshipProviderId") internshipProviderId: Long
    ): String? {
        return fileService.saveInternshipProviderPicture(internshipProviderId, internshipProviderPicture)
    }

//    @Secured(UserRole.Companion.SpringRole.ADMIN, UserRole.Companion.SpringRole.REGULAR_USER)
//    @GetMapping("/all")
//    fun getAll(): List<InternshipProviderResponseDto> {
//        return internshipProviderService.getAll()
//                .map { company -> InternshipProviderResponseDto.fromInternshipProvider(company) }
//    }
}