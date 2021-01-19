package ee.developest.studentpraxis.handler

import ee.developest.studentpraxis.dto.ErrorDto
import ee.developest.studentpraxis.exception.InvalidEmailException
import ee.developest.studentpraxis.exception.InvalidPasswordException
import ee.developest.studentpraxis.exception.UserNotFoundException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

@ControllerAdvice
class ControllerAdviceRequestError : ResponseEntityExceptionHandler() {
    @ExceptionHandler(value = [InvalidEmailException::class, InvalidPasswordException::class, UserNotFoundException::class])
    fun handleUserAlreadyExists(ex: Exception,request: WebRequest): ResponseEntity<ErrorDto> {
        val errorDetails = ErrorDto(ex.message?: "Error")
        return ResponseEntity(errorDetails, HttpStatus.BAD_REQUEST)
    }
}