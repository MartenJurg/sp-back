package ee.developest.studentpraxis.controller

import ee.developest.studentpraxis.dto.CandidatureDto
import ee.developest.studentpraxis.dto.VanillaCandidatureDto
import ee.developest.studentpraxis.dto.response.AdvertisementDto
import ee.developest.studentpraxis.model.Candidature
import ee.developest.studentpraxis.model.UserRole
import ee.developest.studentpraxis.model.VanillaCandidature
import ee.developest.studentpraxis.security.JwtTokenProvider
import ee.developest.studentpraxis.service.CandidateService
import org.springframework.security.access.annotation.Secured
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("candidature")
class CandidateController(
        private val candidateService: CandidateService,
        private val jwtTokenProvider: JwtTokenProvider) {

    @Secured(UserRole.Companion.SpringRole.ADMIN, UserRole.Companion.SpringRole.REGULAR_USER)
    @PostMapping("/{advertisementId}")
    fun candidate(
            @PathVariable advertisementId: Long,
            @RequestHeader(name="Authorization") token: String) {
        candidateService.save(jwtTokenProvider.getUserIdFromToken(jwtTokenProvider.removeBearerFromToken(token))!!, advertisementId)
    }

    @PostMapping("/vanilla")
    fun saveAdvertisementImg(
            @RequestParam("cv") cv: MultipartFile?,
            @RequestParam("advertisementId") advertisementId: Long,
            @RequestParam("email") email: String,
            @RequestParam("linkedin") linkedin: String
    ) {
        candidateService.saveVanillaCandidature(advertisementId, email, linkedin, cv)
    }

    @Secured(UserRole.Companion.SpringRole.ADMIN, UserRole.Companion.SpringRole.REGULAR_USER)
    @GetMapping("/{advertisementId}")
    fun getCandidature(
            @RequestHeader(name="Authorization") token: String,
            @PathVariable advertisementId: Long
    ): List<CandidatureDto> {
        return candidateService.getCandidature(jwtTokenProvider.getUserIdFromToken(jwtTokenProvider.removeBearerFromToken(token))!!, advertisementId)
    }

    @Secured(UserRole.Companion.SpringRole.ADMIN, UserRole.Companion.SpringRole.REGULAR_USER)
    @GetMapping("/vanilla/{advertisementId}")
    fun getVanillaCandidature(
            @RequestHeader(name="Authorization") token: String,
            @PathVariable advertisementId: Long
    ): List<VanillaCandidatureDto> {
        return candidateService.getVanillaCandidature(jwtTokenProvider.getUserIdFromToken(jwtTokenProvider.removeBearerFromToken(token))!!, advertisementId)
    }
}