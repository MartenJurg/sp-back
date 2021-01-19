package ee.developest.studentpraxis.controller

import ee.developest.studentpraxis.dto.request.AuthRequestDto
import ee.developest.studentpraxis.dto.response.AuthResponseDto
import ee.developest.studentpraxis.exception.InvalidEmailException
import ee.developest.studentpraxis.exception.InvalidPasswordException
import ee.developest.studentpraxis.exception.UserNotFoundException
import ee.developest.studentpraxis.model.Intern
import ee.developest.studentpraxis.model.User
import ee.developest.studentpraxis.security.JwtTokenProvider
import ee.developest.studentpraxis.service.InternService
import ee.developest.studentpraxis.service.UserService
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("auth")
class AuthController (
        private val authenticationManager: AuthenticationManager,
        private val jwtTokenProvider: JwtTokenProvider,
        private val passwordEncoder: PasswordEncoder,
        private val userService: UserService,
        private val internService: InternService
) {

    @Throws(InvalidEmailException::class, InvalidPasswordException::class)
    @PostMapping("/register")
    fun register(@RequestBody authRequestDto: AuthRequestDto): AuthResponseDto {
        if (!userService.isValidEmail(authRequestDto.email)) throw InvalidEmailException("Invalid email!")
        if (!userService.isValidPassword(authRequestDto.password)) throw InvalidPasswordException("Password too weak!")

        val user = userService.saveUser(User.fromAuthRequestDto(authRequestDto, passwordEncoder))
        internService.save(Intern.baseIntern(user))
        val token = jwtTokenProvider.generateToken(user)
        return AuthResponseDto(token)
    }

    @Throws(UserNotFoundException::class)
    @PostMapping("/login")
    fun login(@RequestBody authRequestDto: AuthRequestDto): AuthResponseDto {
        // TODO Verify inputs? or done? test...
        authenticationManager.authenticate(UsernamePasswordAuthenticationToken(authRequestDto.email, authRequestDto.password))
        val user = userService.findUserByEmail(authRequestDto.email)
        val token = jwtTokenProvider.generateToken(user)
        return AuthResponseDto(token);
    }
}
