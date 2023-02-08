package co.mvpmatch.backendtask1.web.api

import co.mvpmatch.backendtask1.helper.AuthenticationHelper
import co.mvpmatch.backendtask1.helper.GeneralHelper
import co.mvpmatch.backendtask1.helper.ProductTestHelper
import co.mvpmatch.backendtask1.web.api.model.BuyPayload
import co.mvpmatch.backendtask1.web.api.model.ProductDTO
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.fail
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.setup.MockMvcBuilders

@SpringBootTest
class BuyApiIntTest {
    private var productMockMvc: MockMvc? = null
    private var buyMockMvc: MockMvc? = null

    @Autowired
    private lateinit var productHelper: ProductTestHelper

    @Autowired
    protected lateinit var productController: ProductApiImpl

    @Autowired
    private lateinit var buyApiImpl: BuyApiImpl

    @Autowired
    private lateinit var authenticationHelper: AuthenticationHelper

    @BeforeEach
    fun setup() {
        productMockMvc = MockMvcBuilders.standaloneSetup(productController).build()
        buyMockMvc = MockMvcBuilders.standaloneSetup(buyApiImpl).build()
        authenticationHelper.clearUsers()
    }

    @Test
    fun `buy process works correctly`() {
        Assertions.assertNotNull(productMockMvc)
        val sellerToken = authenticationHelper.getMockSeller1Token()
        productHelper.createProduct(productMockMvc!!, sellerToken)

        val buyerToken = authenticationHelper.getMockBuyer1Token()
        val product = productHelper.testAndGetProduct(productMockMvc!!, buyerToken) ?: fail("Unable to fetch product")
        buyProduct(buyerToken, product)
    }

    private fun buyProduct(buyerToken: String, product: ProductDTO) {
        try {
            Assertions.assertNotNull(buyMockMvc)

            val payload = BuyPayload().apply {
                productId = product.id
                productAmount = 4
            }

            buyMockMvc!!.perform(
                MockMvcRequestBuilders.post("/api/buy")
                    .header("Authorization", "Bearer $buyerToken")
                    .content(GeneralHelper.serializeJson(payload))
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
            )
                .andExpect(MockMvcResultMatchers.status().isOk)
        } catch (e: Exception) {
            GeneralHelper.throwBestException(e)
        }
    }
}
