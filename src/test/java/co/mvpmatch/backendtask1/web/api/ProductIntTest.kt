package co.mvpmatch.backendtask1.web.api

import co.mvpmatch.backendtask1.config.UserNotAllowedException
import co.mvpmatch.backendtask1.helper.AuthenticationHelper
import co.mvpmatch.backendtask1.helper.GeneralHelper.Companion.serializeJson
import co.mvpmatch.backendtask1.helper.GeneralHelper.Companion.throwBestException
import co.mvpmatch.backendtask1.web.api.model.ProductModifyPayload
import org.springframework.security.access.AccessDeniedException
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.setup.MockMvcBuilders

@SpringBootTest
class ProductIntTest {
    private var mockMvc: MockMvc? = null
    private val testProductName = "Mineral Water"
    private val testProductNameUpdated = "Updated Mineral Water"
    private val testAmountAvailable = 10
    private val testCost = 150

    @Autowired
    private lateinit var productController: ProductApiImpl

    @Autowired
    private lateinit var authenticationHelper: AuthenticationHelper

    @BeforeEach
    fun setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(productController).build()
        authenticationHelper.clearUsers()
    }

    @Test
    fun `product crud is working`() {
        val sellerToken = authenticationHelper.getMockSeller1Token()
        createProduct(sellerToken)
        updateProduct(sellerToken)
        deleteProduct(sellerToken, testProductNameUpdated)
    }

    @Test
    fun `only owner of product can modify it`() {
        val seller1Token = authenticationHelper.getMockSeller1Token()
        createProduct(seller1Token)

        val seller2Token = authenticationHelper.getMockSeller2Token()
        assertThrows<UserNotAllowedException> {
            updateProduct(seller2Token)
        }
    }

    @Test
    fun `buyer is not allowed to add product`() {
        val buyer1Token = authenticationHelper.getMockBuyer1Token()
        assertThrows<AccessDeniedException> {
            createProduct(buyer1Token)
        }
    }

    private fun createProduct(userToken: String) {
        try {
            Assertions.assertNotNull(mockMvc)
            val payload = ProductModifyPayload().apply {
                productName = testProductName
                amountAvailable = testAmountAvailable
                cost = testCost
            }

            mockMvc!!.perform(
                MockMvcRequestBuilders.post("/api/product")
                    .header("Authorization", "Bearer $userToken")
                    .content(serializeJson(payload))
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
            )
                .andExpect(MockMvcResultMatchers.status().isCreated)
        } catch (e: Exception) {
            throwBestException(e)
        }
    }

    private fun updateProduct(userToken: String) {
        try {
            Assertions.assertNotNull(mockMvc)
            val payload = ProductModifyPayload().apply {
                productName = testProductNameUpdated
            }
            mockMvc!!.perform(
                MockMvcRequestBuilders.put("/api/product/{productName}", testProductName)
                    .header("Authorization", "Bearer $userToken")
                    .content(serializeJson(payload))
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
            )
                .andExpect(MockMvcResultMatchers.status().isOk)
        } catch (e: Exception) {
            throwBestException(e)
        }
    }

    private fun deleteProduct(userToken: String, prodName: String = testProductName) {
        try {
            Assertions.assertNotNull(mockMvc)
            mockMvc!!.perform(
                MockMvcRequestBuilders.delete("/api/product/{productName}", prodName)
                    .header("Authorization", "Bearer $userToken")
            )
                .andExpect(MockMvcResultMatchers.status().isNoContent)
        } catch (e: Exception) {
            throwBestException(e)
        }
    }


}
