package ee.developest.studentpraxis.service

import ee.developest.studentpraxis.model.ContactType
import ee.developest.studentpraxis.repository.ContactTypeRepository
import org.springframework.stereotype.Service

@Service
class ContactTypeService(private val contactTypeRepository: ContactTypeRepository) {

    fun getEmail(): ContactType {
        return contactTypeRepository.getOne(1)
    }

    fun getPhone(): ContactType {
        return contactTypeRepository.getOne(2)
    }

    fun getLinkedIn(): ContactType {
        return contactTypeRepository.getOne(3)
    }

    fun getAddress(): ContactType {
        return contactTypeRepository.getOne(4)
    }
}