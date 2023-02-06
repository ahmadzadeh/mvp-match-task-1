package co.mvpmatch.backendtask1

import co.mvpmatch.backendtask1.config.ApplicationProperties
import co.mvpmatch.backendtask1.config.security.TokenProvider
import co.mvpmatch.backendtask1.web.api.model.LoginResponseDTO
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service

@Service
class AuthenticationService(
    private val authenticationProvider: AuthenticationProvider,
    private val tokenProvider: TokenProvider,
    private val prop: ApplicationProperties
) {
    fun authenticate(credentials: UsernamePasswordAuthenticationToken): LoginResponseDTO {
        val authentication: Authentication = authenticationProvider.authenticate(credentials)
        SecurityContextHolder.getContext().authentication = authentication
        val jwt = tokenProvider.createToken(authentication)
        return LoginResponseDTO().apply {
            token = jwt
            expiresIn = prop.security.tokenValidityInSeconds

        }
    }
}
