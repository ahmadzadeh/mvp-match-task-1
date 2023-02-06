package co.mvpmatch.backendtask1.helper

import co.mvpmatch.backendtask1.AuthenticationService
import co.mvpmatch.backendtask1.helper.UserHelper.Companion.TEST_PASSWORD
import org.junit.BeforeClass
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import co.mvpmatch.backendtask1.domain.User as UserEntity
import org.springframework.stereotype.Service

@Service
class AuthenticationHelper {
    @Autowired
    private lateinit var userHelper: UserHelper

    @Autowired
    private lateinit var authorityHelper: AuthorityHelper

    @BeforeClass
    fun init() {
        authorityHelper.insertAuthoritiesIfNotExist()
    }

    @Autowired
    private lateinit var authenticationService: AuthenticationService

    fun getAccessToken(user: UserEntity): String {
        val payload = UsernamePasswordAuthenticationToken(
            user.username,
            TEST_PASSWORD
        )
        return authenticationService.authenticate(payload).token
    }

    fun getMockAdminToken(): String {
        return getAccessToken(userHelper.getTestAdminUser())
    }

    fun getMockBuyerToken(): String {
        return getAccessToken(userHelper.getTestBuyer())
    }

    fun getMockSellerToken(): String {
        return getAccessToken(userHelper.getTestSeller())
    }
}
