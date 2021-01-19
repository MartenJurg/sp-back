package ee.developest.studentpraxis.service

import ee.developest.studentpraxis.dto.request.ProfileRequestDto
import ee.developest.studentpraxis.dto.response.ProfileResponseDto
import ee.developest.studentpraxis.model.UserContact
import org.springframework.stereotype.Service
import java.util.*

@Service
class ProfileService(
        private val internService: InternService,
        private val userContactService: UserContactService,
        private val contactTypeService: ContactTypeService,
        private val userService: UserService,
        private val fileService: FileService
) {

    fun updatePersonalData(userId: Long, profileRequestDto: ProfileRequestDto) {
        internService.update(userId, profileRequestDto.full_name, profileRequestDto.about)
        if (profileRequestDto.phone != null) {
            userContactService.add(UserContact(
                    value = profileRequestDto.phone,
                    user = userService.findUserById(userId),
                    contactType = contactTypeService.getPhone(),
                    createdTimestamp = Date()))
        }

        if (profileRequestDto.linkedInUrl != null) {
            userContactService.add(UserContact(
                    value = profileRequestDto.linkedInUrl,
                    user = userService.findUserById(userId),
                    contactType = contactTypeService.getLinkedIn(),
                    createdTimestamp = Date()))
        }

        // TODO IMG
    }

    fun getPersonalData(userId: Long): ProfileResponseDto {
        val intern = internService.findById(userId)
        val phone = userContactService.findLastByUserIdAndType(userId, contactTypeService.getPhone())
        val linkedInUrl = userContactService.findLastByUserIdAndType(userId, contactTypeService.getLinkedIn())
        return ProfileResponseDto(
                full_name = intern.full_name,
                about = intern.about,
                phone = phone?.value,
                linkedInUrl = linkedInUrl?.value)
    }
}