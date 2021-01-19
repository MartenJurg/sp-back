package ee.developest.studentpraxis.controller

import ee.developest.studentpraxis.dto.UserDto
import ee.developest.studentpraxis.model.UserRole
import ee.developest.studentpraxis.service.UserService
import org.springframework.security.access.annotation.Secured
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.stream.Collectors

@RestController
@RequestMapping("user")
class UserController(private val userService: UserService) {

    // TODO: These are dummy methods for testing

    @Secured(UserRole.Companion.SpringRole.ADMIN)
    @GetMapping("/all")
    fun getUsers(): List<UserDto> {
        return userService.getAll().map { u -> UserDto.fromModel(u) }
    }

    @Secured(UserRole.Companion.SpringRole.ADMIN, UserRole.Companion.SpringRole.REGULAR_USER)
    @GetMapping
    fun getSingle(): List<UserDto> {
        return userService.getAll()
                .stream()
                .limit(1)
                .map { u -> UserDto.fromModel(u) }
                .collect(Collectors.toList())
    }
}