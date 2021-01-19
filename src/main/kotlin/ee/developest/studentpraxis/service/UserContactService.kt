package ee.developest.studentpraxis.service

import ee.developest.studentpraxis.model.ContactType
import ee.developest.studentpraxis.model.UserContact
import ee.developest.studentpraxis.repository.ContactRepository
import org.springframework.stereotype.Service
import java.util.*

@Service
class UserContactService(private val userContactRepository: ContactRepository) {

    fun add(userContact: UserContact): UserContact {
        return userContactRepository.save(userContact)
    }

    fun findLastByUserIdAndType(userId: Long, userContactType: ContactType): UserContact? {
        return userContactRepository.findFirstByUserIdAndContactTypeIdOrderByCreatedTimestampDesc(userId, userContactType.id)
    }
}