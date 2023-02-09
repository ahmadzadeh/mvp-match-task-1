package co.mvpmatch.backendtask1.web.api

import co.mvpmatch.backendtask1.AuthenticationService
import co.mvpmatch.backendtask1.config.UnauthorizedException
import co.mvpmatch.backendtask1.config.security.getCurrentBasicAuthCredentials
import co.mvpmatch.backendtask1.config.security.getCurrentUserLogin
import co.mvpmatch.backendtask1.web.api.model.LoginResponseDTO
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api")
class LoginApiImpl(
    private val authenticationService: AuthenticationService
) : LoginApi, LogoutApi {

    override fun _login(): ResponseEntity<LoginResponseDTO> {
        val credentials = getCurrentBasicAuthCredentials() ?: throw UnauthorizedException()
        return ResponseEntity.ok(
            authenticationService.authenticate(credentials)
        )
    }

    override fun _terminateAllSessions(): ResponseEntity<Void> {
        authenticationService.terminateUserSessions(
            getCurrentUserLogin() ?: throw UnauthorizedException()
        )
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build()
    }
}
