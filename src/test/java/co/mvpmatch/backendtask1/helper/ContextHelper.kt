package co.mvpmatch.backendtask1.helper

import co.mvpmatch.backendtask1.AuthenticationService
import co.mvpmatch.backendtask1.helper.UserHelper.Companion.TEST_PASSWORD
import co.mvpmatch.backendtask1.repository.BuyHistoryRepository
import co.mvpmatch.backendtask1.repository.ProductRepository
import org.junit.BeforeClass
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import co.mvpmatch.backendtask1.domain.User as UserEntity
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ContextHelper {
    @Autowired
    private lateinit var userHelper: UserHelper

    @Autowired
    private lateinit var authorityHelper: AuthorityHelper

    @Autowired
    private lateinit var productRepository: ProductRepository

    @Autowired
    private lateinit var buyHistoryRepository: BuyHistoryRepository

    @BeforeClass
    fun init() {
        authorityHelper.insertAuthoritiesIfNotExist()
    }

    @Autowired
    private lateinit var authenticationService: AuthenticationService

    fun getAccessTokenWithDefaultPassword(user: UserEntity): String {
        return getAccessToken(user.username, TEST_PASSWORD)
    }

    fun getAccessToken(username: String, rawPassword: String): String {
        val payload = UsernamePasswordAuthenticationToken(
            username,
            rawPassword
        )
        return authenticationService.authenticate(payload).token
    }

    fun getMockAdminToken(): String {
        return getAccessTokenWithDefaultPassword(userHelper.getTestAdminUser())
    }

    fun getMockBuyer1Token(): String {
        return getAccessTokenWithDefaultPassword(userHelper.getTestBuyer1())
    }

    fun getMockSeller1Token(): String {
        return getAccessTokenWithDefaultPassword(userHelper.getTestSeller1())
    }

    fun getMockBuyer2Token(): String {
        return getAccessTokenWithDefaultPassword(userHelper.getTestBuyer2())
    }

    fun getMockSeller2Token(): String {
        return getAccessTokenWithDefaultPassword(userHelper.getTestSeller2())
    }

    @Transactional
    fun clearData() {
        buyHistoryRepository.deleteAll()
        productRepository.deleteAll()
        userHelper.clearUsers()
    }
}
