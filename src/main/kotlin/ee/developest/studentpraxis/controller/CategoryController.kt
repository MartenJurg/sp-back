package ee.developest.studentpraxis.controller

import ee.developest.studentpraxis.dto.InternshipCategoryDto
import ee.developest.studentpraxis.model.UserRole
import ee.developest.studentpraxis.service.InternshipCategoryService
import org.springframework.security.access.annotation.Secured
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("category")
class CategoryController(private val categoryService: InternshipCategoryService) {

    @GetMapping("/all")
    fun getAll(): List<InternshipCategoryDto> {
        return categoryService.getAll()
    }

    @Secured(UserRole.Companion.SpringRole.ADMIN)
    @PostMapping
    fun addCategory(@RequestParam name: String) {
        return categoryService.addCategory(name)
    }
}