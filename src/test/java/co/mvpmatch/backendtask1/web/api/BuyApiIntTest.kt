package co.mvpmatch.backendtask1.web.api

import co.mvpmatch.backendtask1.helper.AuthenticationHelper
import co.mvpmatch.backendtask1.helper.GeneralHelper
import co.mvpmatch.backendtask1.helper.ProductTestHelper
import co.mvpmatch.backendtask1.helper.ProductTestHelper.Companion.testAmountToBuy
import co.mvpmatch.backendtask1.helper.ProductTestHelper.Companion.testProductCost
import co.mvpmatch.backendtask1.web.api.model.BuyPayload
import co.mvpmatch.backendtask1.web.api.model.BuyResponseDTO
import co.mvpmatch.backendtask1.web.api.model.DepositPayload
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
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import co.mvpmatch.backendtask1.web.api.model.DepositPayload.AmountsInCentEnum as AmountsInCentEnum

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
        val product = productHelper.testAndGetProduct(productMockMvc!!, buyerToken)
            ?: fail("Unable to fetch product")

        //We add 200 Cents credit and buy test product with a cost of 165 cents
        addCredit(buyerToken, AmountsInCentEnum.NUMBER_100)
        addCredit(buyerToken, AmountsInCentEnum.NUMBER_50)
        addCredit(buyerToken, AmountsInCentEnum.NUMBER_50)

        val buyResponse = buyProduct(buyerToken, product)
        assertNotNull(buyResponse)
        assertEquals(buyResponse.productId, product.id)
        assertEquals(buyResponse.totalCost, testAmountToBuy * testProductCost)
        assertEquals(buyResponse.changedCoins.toSet(), setOf(20, 10, 5)) //order does not matter for set, sum must be 35
    }

    private fun addCredit(buyerToken: String, coin: DepositPayload.AmountsInCentEnum) {
        try {
            Assertions.assertNotNull(buyMockMvc)

            val payload = DepositPayload().apply {
                amountsInCent = coin
            }

            buyMockMvc!!.perform(
                MockMvcRequestBuilders.post("/api/deposit")
                    .header("Authorization", "Bearer $buyerToken")
                    .content(GeneralHelper.serializeJson(payload))
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
            )
                .andExpect(MockMvcResultMatchers.status().isOk)
        } catch (e: Exception) {
            GeneralHelper.throwBestException(e)
        }
    }

    private fun buyProduct(buyerToken: String, product: ProductDTO): BuyResponseDTO {
        try {
            Assertions.assertNotNull(buyMockMvc)

            val payload = BuyPayload().apply {
                productId = product.id
                productAmount = testAmountToBuy
            }

            val result = buyMockMvc!!.perform(
                MockMvcRequestBuilders.post("/api/buy")
                    .header("Authorization", "Bearer $buyerToken")
                    .content(GeneralHelper.serializeJson(payload))
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
            )
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andReturn()

            assertNotNull(result.response.contentAsString)
            return GeneralHelper.deserializeJson(result.response.contentAsString)

        } catch (e: Exception) {
            GeneralHelper.throwBestException(e)
        }
        fail("Failed to get buyResponse()")
    }
}
