package co.mvpmatch.backendtask1.config.security

import co.mvpmatch.backendtask1.config.ResourceNotFoundException
import co.mvpmatch.backendtask1.config.UserNotActivatedException
import co.mvpmatch.backendtask1.domain.User
import co.mvpmatch.backendtask1.repository.UserRepository
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service
import org.springframework.security.core.userdetails.User as SpringUser
import java.util.stream.Collectors

@Service("userDetailsService")
class UserDetailsService(
    private val userRepository: UserRepository
) : UserDetailsService {
    override fun loadUserByUsername(username: String): UserDetails {
        val user = userRepository.findOneByUsernameIgnoreCase(username)
            .orElseThrow { ResourceNotFoundException() }
        return createSpringSecurityUser(user)
    }

    private fun createSpringSecurityUser(user: User): SpringUser {
        if (!user.activated) {
            throw UserNotActivatedException("User ${user.username} was not activated")
        }
        val grantedAuthorities: List<GrantedAuthority> = user.authorities.stream()
            .map { authority -> SimpleGrantedAuthority(authority.name) }
            .collect(Collectors.toList())
        return SpringUser(
            user.username.lowercase(),
            user.passwordHash,
            grantedAuthorities
        )
    }
}
