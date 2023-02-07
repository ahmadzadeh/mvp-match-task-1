package co.mvpmatch.backendtask1.config.security

import co.mvpmatch.backendtask1.config.AUTHORITIES_KEY
import co.mvpmatch.backendtask1.config.ApplicationProperties
import co.mvpmatch.backendtask1.config.UnauthorizedException
import io.jsonwebtoken.*
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.slf4j.LoggerFactory
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.stereotype.Component
import java.security.Key
import java.util.*
import java.util.stream.Collectors
import kotlin.jvm.Throws

@Component
class TokenProvider(
    private val prop: ApplicationProperties,
    private val saltProvider: SaltProvider
) {
    private val log = LoggerFactory.getLogger(javaClass)
    private val unsignedJwtParser: JwtParser = Jwts.parserBuilder().build()

    fun createToken(authentication: Authentication): String {
        //using salt gives us control to invalidate user token, even if it is not expired.
        val userSalt = saltProvider.getSalt(authentication.name)
            ?: throw UnauthorizedException()

        val signingKeyWithSalt = signingKeyWithSalt(userSalt)
        val authorities = authentication.authorities.stream()
            .map { obj: GrantedAuthority -> obj.authority }
            .collect(Collectors.joining(","))
        val now = Date().time
        val validity = Date(now + 1000 * prop.security.tokenValidityInSeconds)
        return Jwts.builder()
            .setSubject(authentication.name)
            .claim(AUTHORITIES_KEY, authorities)
            .signWith(signingKeyWithSalt, SignatureAlgorithm.HS512)
            .setExpiration(validity)
            .compact()
    }

    fun getAuthentication(token: String?): Authentication {
        val claims = parseClaims(token)
        val authorities: Set<GrantedAuthority> = claims[AUTHORITIES_KEY]
            .toString().split(",")
            .map { role -> SimpleGrantedAuthority(role) }
            .toSet()

        val principal = User(claims.subject, "", authorities)
        return UsernamePasswordAuthenticationToken(principal, token, authorities)
    }

    fun validateToken(authToken: String?): Boolean {
        return try {
            parseClaims(authToken).subject != null
        } catch (e: Exception) {
            false
        }
    }

    @Throws(JwtException::class)
    fun parseClaims(authToken: String?): Claims {
        if (authToken == null) throw JwtException("token is null")
        val unsignedClaims = decodeUnsignedTokenClaims(authToken)
        val userLogin = unsignedClaims.body.subject?.toString() ?: throw JwtException("unable to get subject")
        val userSalt = saltProvider.getSalt(userLogin) ?: throw JwtException("unable to get user salt")
        return Jwts.parserBuilder()
            .setSigningKey(signingKeyWithSalt(userSalt))
            .build().parseClaimsJws(authToken)
            .body
    }

    private fun signingKeyWithSalt(userSalt: String): Key {
        val keyBytes: ByteArray = Decoders.BASE64.decode(prop.security.secret)
        return Keys.hmacShaKeyFor(keyBytes + userSalt.toByteArray())
    }

    fun decodeUnsignedTokenClaims(token: String): Jwt<Header<*>, Claims> {
        // drop signature
        val splitToken =
            token.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val unsignedToken = splitToken[0] + "." + splitToken[1] + "."

        return unsignedJwtParser.parseClaimsJwt(unsignedToken)
    }
}
