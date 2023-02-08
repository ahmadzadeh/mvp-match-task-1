package co.mvpmatch.backendtask1.web.api

import co.mvpmatch.backendtask1.config.UserNotAllowedException
import co.mvpmatch.backendtask1.helper.AuthenticationHelper
import co.mvpmatch.backendtask1.helper.ProductTestHelper
import co.mvpmatch.backendtask1.helper.ProductTestHelper.Companion.testProductNameUpdated
import org.junit.jupiter.api.Assertions
import org.springframework.security.access.AccessDeniedException
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders

@SpringBootTest
class ProductIntTest {
    private var mockMvc: MockMvc? = null

    @Autowired
    private lateinit var productHelper: ProductTestHelper

    @Autowired
    protected lateinit var productController: ProductApiImpl

    @Autowired
    private lateinit var authenticationHelper: AuthenticationHelper

    @BeforeEach
    fun setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(productController).build()
        authenticationHelper.clearUsers()
    }

    @Test
    fun `product crud is working`() {
        Assertions.assertNotNull(mockMvc)
        val sellerToken = authenticationHelper.getMockSeller1Token()
        productHelper.createProduct(mockMvc!!, sellerToken)
        productHelper.testAndGetProduct(mockMvc!!, sellerToken)
        productHelper.updateProduct(mockMvc!!, sellerToken)
        productHelper.deleteProduct(mockMvc!!, sellerToken, testProductNameUpdated)
    }

    @Test
    fun `only owner of product can modify it`() {
        Assertions.assertNotNull(mockMvc)
        val seller1Token = authenticationHelper.getMockSeller1Token()
        productHelper.createProduct(mockMvc!!, seller1Token)

        val seller2Token = authenticationHelper.getMockSeller2Token()
        assertThrows<UserNotAllowedException> {
            productHelper.updateProduct(mockMvc!!, seller2Token)
        }
    }

    @Test
    fun `buyer is not allowed to add product`() {
        Assertions.assertNotNull(mockMvc)
        val buyer1Token = authenticationHelper.getMockBuyer1Token()
        assertThrows<AccessDeniedException> {
            productHelper.createProduct(mockMvc!!, buyer1Token)
        }
    }


}
