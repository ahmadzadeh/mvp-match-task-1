package co.mvpmatch.backendtask1.config

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest


@ControllerAdvice
class ExceptionTranslator {

    @ExceptionHandler(ResourceNotFoundException::class)
    fun handleNotFound(ex: Exception, request: WebRequest): ResponseEntity<String> {
        return ResponseEntity("Requested resource not found", HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler(ResourceAlreadyExistsException::class)
    fun handleAlreadyExists(ex: ResourceAlreadyExistsException, request: WebRequest): ResponseEntity<String> {
        return ResponseEntity("Resource already exists ", HttpStatus.CONFLICT)
    }

    @ExceptionHandler(UserNotAllowedException::class)
    fun handleNotAllowed(ex: UserNotAllowedException, request: WebRequest): ResponseEntity<String> {
        return ResponseEntity("You don't have enough permission to do this action ", HttpStatus.FORBIDDEN)
    }

    @ExceptionHandler(UnauthorizedException::class)
    fun handleUnauthorized(ex: UnauthorizedException, request: WebRequest): ResponseEntity<String> {
        return ResponseEntity("You're not authorized. do login first ", HttpStatus.UNAUTHORIZED)
    }

}
