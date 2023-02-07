package co.mvpmatch.backendtask1.config.security

import co.mvpmatch.backendtask1.config.CACHE_USER_SALT
import co.mvpmatch.backendtask1.repository.UserRepository
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service

@Service
class UserSaltProvider(
    private val userRepository: UserRepository
) : SaltProvider {
    @Cacheable(cacheNames = [CACHE_USER_SALT], key = "#userLogin")
    override fun getSalt(principal: String): String? {
        return userRepository.findOneByUsernameIgnoreCase(principal)
            .orElse(null)?.jwtSalt
    }

}
