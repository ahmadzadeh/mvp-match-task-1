package co.mvpmatch.backendtask1.config.security

import co.mvpmatch.backendtask1.config.ANONYMOUS_USER
import co.mvpmatch.backendtask1.config.AUTHORIZATION_HEADER
import org.apache.commons.lang3.StringUtils
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.util.Base64Utils
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes
import java.nio.charset.StandardCharsets
import java.util.*

fun findCurrentUserLogin(): Optional<String> =
    Optional.ofNullable(extractPrincipal(SecurityContextHolder.getContext().authentication))

fun getCurrentUserLogin(): String? {
    val extractPrincipal = extractPrincipal(SecurityContextHolder.getContext().authentication)
    if (StringUtils.equalsIgnoreCase(ANONYMOUS_USER, extractPrincipal)) return null
    return extractPrincipal
}

fun extractPrincipal(authentication: Authentication?): String? {
    return when (val principal = authentication?.principal) {
        is UserDetails -> principal.username
        is String -> principal
        else -> null
    }
}

fun getCurrentBasicAuthCredentials(): UsernamePasswordAuthenticationToken? {
    val request = (RequestContextHolder.getRequestAttributes() as ServletRequestAttributes).request
    val header = request.getHeader(AUTHORIZATION_HEADER) ?: return null
    val decodedHeader = String(
        Base64Utils.decodeFromString(StringUtils.removeIgnoreCase(header, "Basic ")),
        StandardCharsets.UTF_8
    )

    val credentials: Array<String> = decodedHeader.split(":".toRegex()).toTypedArray()
    return UsernamePasswordAuthenticationToken(credentials[0], credentials[1])
}
