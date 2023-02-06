package co.mvpmatch.backendtask1.config.security

import co.mvpmatch.backendtask1.config.AUTHORITIES_KEY
import co.mvpmatch.backendtask1.config.ApplicationProperties
import io.jsonwebtoken.Claims
import io.jsonwebtoken.JwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
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
import javax.annotation.PostConstruct

@Component
class TokenProvider(private val prop: ApplicationProperties) {
    private val log = LoggerFactory.getLogger(javaClass)

    private var key: Key? = null

    @PostConstruct
    fun init() {
        val keyBytes = Decoders.BASE64.decode(prop.security.secret)
        key = Keys.hmacShaKeyFor(keyBytes)
    }

    fun createToken(authentication: Authentication): String {
        val authorities = authentication.authorities.stream()
            .map { obj: GrantedAuthority -> obj.authority }
            .collect(Collectors.joining(","))
        val now = Date().time
        val validity = Date(now + 1000 * prop.security.tokenValidityInSeconds)
        return Jwts.builder()
            .setSubject(authentication.name)
            .claim(AUTHORITIES_KEY, authorities)
            .signWith(key, SignatureAlgorithm.HS512)
            .setExpiration(validity)
            .compact()
    }

    fun getAuthentication(token: String?): Authentication {
        val claims: Claims = Jwts.parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(token)
            .body

        val authorities: Set<GrantedAuthority> = claims[AUTHORITIES_KEY]
            .toString().split(",")
            .map { role -> SimpleGrantedAuthority(role) }
            .toSet()

        val principal = User(claims.subject, "", authorities)
        return UsernamePasswordAuthenticationToken(principal, token, authorities)
    }

    fun validateToken(authToken: String?): Boolean {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(authToken)
            return true
        } catch (e: JwtException) {
            log.error("Invalid JWT token", e)
        } catch (e: IllegalArgumentException) {
            log.error("Invalid JWT token", e)
        }
        return false
    }
}
