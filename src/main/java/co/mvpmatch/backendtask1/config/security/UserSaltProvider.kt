package co.mvpmatch.backendtask1.config.security

import co.mvpmatch.backendtask1.config.security.SaltProvider
import co.mvpmatch.backendtask1.repository.UserRepository
import org.springframework.stereotype.Service

@Service
class UserSaltProvider(
    private val userRepository: UserRepository
) : SaltProvider {
    // TODO: add cache
    //@Cacheable(cacheNames = [CACHE_USER_SALT], key = "#userLogin")
    override fun getSalt(principal: String): String? {
        return userRepository.findOneByUsernameIgnoreCase(principal)
            .orElse(null)?.jwtSalt
    }

}
