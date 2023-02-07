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

    @ExceptionHandler(InvalidParamException::class)
    fun handleInvalidParamException(ex: InvalidParamException, request: WebRequest): ResponseEntity<String> {
        return ResponseEntity(ex.msg, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(InsufficientUserBalanceException::class)
    fun handleInsufficientUserBalanceException(
        ex: InsufficientUserBalanceException,
        request: WebRequest
    ): ResponseEntity<String> {
        return ResponseEntity("User's balance is not sufficient ", HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(InsufficientProductAmount::class)
    fun handleInsufficientProductAmount(ex: InsufficientProductAmount, request: WebRequest): ResponseEntity<String> {
        return ResponseEntity("Product is not available for the requested amount", HttpStatus.NOT_ACCEPTABLE)
    }

    @ExceptionHandler(UnauthorizedException::class)
    fun handleUnableToChangeCoinsException(
        ex: UnableToChangeCoinsException,
        request: WebRequest
    ): ResponseEntity<String> {
        return ResponseEntity("Unable to exchange coins. purchase will be cancelled.", HttpStatus.CONFLICT)
    }

    @ExceptionHandler(UnauthorizedException::class)
    fun handleUnauthorized(ex: UnauthorizedException, request: WebRequest): ResponseEntity<String> {
        return ResponseEntity("You're not authorized. do login first ", HttpStatus.UNAUTHORIZED)
    }

    @ExceptionHandler(UserNotActivatedException::class)
    fun handleUserNotActivatedException(ex: UserNotActivatedException, request: WebRequest): ResponseEntity<String> {
        return ResponseEntity("User is not activated ", HttpStatus.FORBIDDEN)
    }


}
