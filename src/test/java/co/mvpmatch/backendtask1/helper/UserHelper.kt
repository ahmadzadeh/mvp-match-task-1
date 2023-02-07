package co.mvpmatch.backendtask1.helper

import co.mvpmatch.backendtask1.config.ROLE_ADMIN
import co.mvpmatch.backendtask1.config.ROLE_BUYER
import co.mvpmatch.backendtask1.config.ROLE_SELLER
import co.mvpmatch.backendtask1.domain.Authority
import co.mvpmatch.backendtask1.domain.User
import co.mvpmatch.backendtask1.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@SpringBootTest
class UserHelper {
    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var passwordEncoder: PasswordEncoder

    companion object {
        const val TEST_USER_NAME = "test_username"
        const val TEST_FULL_NAME = "test name"
        const val TEST_PASSWORD = "Pa55w0rd"
        const val TEST_JWT_SALT = "jwtSalt"
        const val TEST_LANG_KEY = "en"
        const val TEST_CREDIT = 0
    }

    fun getTestAdminUser(): User {
        val username = "admin_$TEST_USER_NAME"
        return userRepository.findOneByUsernameIgnoreCase(username)
            .orElse(insertTestUser(username, setOf(Authority(ROLE_ADMIN))))
    }

    fun getTestBuyer1(): User {
        val username = "buyer1_$TEST_USER_NAME"
        return userRepository.findOneByUsernameIgnoreCase(username)
            .orElse(insertTestUser(username, setOf(Authority(ROLE_BUYER))))
    }

    fun getTestBuyer2(): User {
        val username = "buyer2_$TEST_USER_NAME"
        return userRepository.findOneByUsernameIgnoreCase(username)
            .orElse(insertTestUser(username, setOf(Authority(ROLE_BUYER))))
    }

    fun getTestSeller1(): User {
        val username = "seller1_$TEST_USER_NAME"
        return userRepository.findOneByUsernameIgnoreCase(username)
            .orElse(insertTestUser(username, setOf(Authority(ROLE_SELLER))))
    }

    fun getTestSeller2(): User {
        val username = "seller2_$TEST_USER_NAME"
        return userRepository.findOneByUsernameIgnoreCase(username)
            .orElse(insertTestUser(username, setOf(Authority(ROLE_SELLER))))
    }

    @Transactional
    fun insertTestUser(username: String, authorities: Set<Authority>): User {
        return userRepository.save(
            User(
                username = username,
                passwordHash = passwordEncoder.encode(TEST_PASSWORD),
                jwtSalt = TEST_JWT_SALT,
                fullName = TEST_FULL_NAME,
                activated = true,
                authorities = authorities.toMutableSet(),
                langKey = TEST_LANG_KEY,
                credit = TEST_CREDIT
            )
        )
    }

    @Transactional
    fun clearUsers() {
        userRepository.deleteAll()
    }
}
