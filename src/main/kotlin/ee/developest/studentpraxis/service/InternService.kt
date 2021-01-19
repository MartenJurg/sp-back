package ee.developest.studentpraxis.service

import ee.developest.studentpraxis.model.Intern
import ee.developest.studentpraxis.repository.InternRepository
import org.springframework.stereotype.Service
import java.util.*

@Service
class InternService(private val internRepository: InternRepository) {

    fun save(intern: Intern): Intern {
        return internRepository.save(intern)
    }

    fun update(internId: Long, full_name: String?, about: String?) {
        var intern: Intern = internRepository.findById(internId).get()
        if (full_name != null && full_name != "") {
            intern.full_name = full_name
        }
        if (about != null && about != "") {
            intern.about = about
        }
        internRepository.save(intern)
    }

    fun findById(internId: Long): Intern {
        return internRepository.findById(internId).orElse(null) ?: throw Exception("Intern not found")
    }
}