package ee.developest.studentpraxis.dto

import ee.developest.studentpraxis.model.InternshipCategory

class InternshipCategoryDto(
        val id: Long,
        val name: String
) {
    companion object {
        fun fromInternshipCategory(cat: InternshipCategory): InternshipCategoryDto {
            return InternshipCategoryDto(id = cat.id, name = cat.name)
        }
    }
}