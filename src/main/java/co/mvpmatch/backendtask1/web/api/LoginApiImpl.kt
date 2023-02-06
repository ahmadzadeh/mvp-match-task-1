package co.mvpmatch.backendtask1.web.api

import co.mvpmatch.backendtask1.AuthenticationService
import co.mvpmatch.backendtask1.config.UnauthorizedException
import co.mvpmatch.backendtask1.config.security.getCurrentBasicAuthCredentials
import co.mvpmatch.backendtask1.web.api.model.LoginResponseDTO
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api")
class LoginApiImpl(
    private val authenticationService: AuthenticationService
) : LoginApi {

    override fun _login(): ResponseEntity<LoginResponseDTO> {
        val credentials = getCurrentBasicAuthCredentials() ?: throw UnauthorizedException()
        return ResponseEntity.ok(
            authenticationService.authenticate(credentials)
        )
    }

}
