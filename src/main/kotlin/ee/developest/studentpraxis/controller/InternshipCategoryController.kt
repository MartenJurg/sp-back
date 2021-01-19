package ee.developest.studentpraxis.controller

import ee.developest.studentpraxis.dto.InternshipCategoryDto
import ee.developest.studentpraxis.model.UserRole
import ee.developest.studentpraxis.service.InternshipCategoryService
import org.springframework.security.access.annotation.Secured
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("category")
class InternshipCategoryController(
        private val internshipCategoryService: InternshipCategoryService
) {

    @Secured(UserRole.Companion.SpringRole.ADMIN, UserRole.Companion.SpringRole.REGULAR_USER)
    @GetMapping
    fun getCategories(): List<InternshipCategoryDto> {
        return internshipCategoryService.getAll()
    }
}