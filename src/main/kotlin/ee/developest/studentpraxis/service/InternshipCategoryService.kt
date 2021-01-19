package ee.developest.studentpraxis.service


import ee.developest.studentpraxis.dto.InternshipCategoryDto
import ee.developest.studentpraxis.model.InternshipCategory
import ee.developest.studentpraxis.repository.InternshipCategoryRepository
import org.springframework.stereotype.Service
import java.util.*

@Service
class InternshipCategoryService(
        private val internshipCategoryRepository: InternshipCategoryRepository
) {
    fun getAll(): List<InternshipCategoryDto> {
        return internshipCategoryRepository.findAll()
                .map {internshipCategory -> InternshipCategoryDto(id = internshipCategory.id, name = internshipCategory.name)  }
    }

    fun findById(categoryId: Long): Optional<InternshipCategory> {
        return internshipCategoryRepository.findById(categoryId)
    }

    fun addCategory(name: String) {
        internshipCategoryRepository.save(InternshipCategory(name = name))
    }
}