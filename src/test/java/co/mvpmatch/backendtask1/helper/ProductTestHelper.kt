package co.mvpmatch.backendtask1.helper

import co.mvpmatch.backendtask1.helper.GeneralHelper.Companion.deserializeJson
import co.mvpmatch.backendtask1.web.api.model.ProductDTO
import co.mvpmatch.backendtask1.web.api.model.ProductModifyPayload
import org.junit.jupiter.api.Assertions
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.fail

@Component
class ProductTestHelper {
    companion object {
        val testProductName = "Mineral Water"
        val testProductNameUpdated = "Updated Mineral Water"
        val testAmountAvailable = 10
        val testCost = 165
    }

    fun createProduct(mockMvc: MockMvc, userToken: String) {
        try {
            Assertions.assertNotNull(mockMvc)
            val payload = ProductModifyPayload().apply {
                productName = testProductName
                amountAvailable = testAmountAvailable
                cost = testCost
            }

            mockMvc.perform(
                MockMvcRequestBuilders.post("/api/product")
                    .header("Authorization", "Bearer $userToken")
                    .content(GeneralHelper.serializeJson(payload))
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
            )
                .andExpect(MockMvcResultMatchers.status().isCreated)
        } catch (e: Exception) {
            GeneralHelper.throwBestException(e)
        }
    }

    fun testAndGetProduct(mockMvc: MockMvc, userToken: String): ProductDTO? {
        try {
            Assertions.assertNotNull(mockMvc)
            val result = mockMvc.perform(
                MockMvcRequestBuilders.get("/api/product/{productName}", testProductName)
                    .header("Authorization", "Bearer $userToken")
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
            )
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andReturn()

            assertNotNull(result.response.contentAsString)
            val productDTO = deserializeJson<ProductDTO>(result.response.contentAsString)
            assertEquals(productDTO.productName, testProductName)
            return productDTO
        } catch (e: Exception) {
            GeneralHelper.throwBestException(e)
        }
        fail("testAndGetProduct returned null")
    }

    fun updateProduct(mockMvc: MockMvc, userToken: String) {
        try {
            Assertions.assertNotNull(mockMvc)
            val payload = ProductModifyPayload().apply {
                productName = testProductNameUpdated
            }
            mockMvc.perform(
                MockMvcRequestBuilders.put("/api/product/{productName}", testProductName)
                    .header("Authorization", "Bearer $userToken")
                    .content(GeneralHelper.serializeJson(payload))
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
            )
                .andExpect(MockMvcResultMatchers.status().isOk)
        } catch (e: Exception) {
            GeneralHelper.throwBestException(e)
        }
    }

    fun deleteProduct(mockMvc: MockMvc, userToken: String, prodName: String = testProductName) {
        try {
            Assertions.assertNotNull(mockMvc)
            mockMvc.perform(
                MockMvcRequestBuilders.delete("/api/product/{productName}", prodName)
                    .header("Authorization", "Bearer $userToken")
            )
                .andExpect(MockMvcResultMatchers.status().isNoContent)
        } catch (e: Exception) {
            GeneralHelper.throwBestException(e)
        }
    }
}
