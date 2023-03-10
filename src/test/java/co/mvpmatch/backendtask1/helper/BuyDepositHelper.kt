package co.mvpmatch.backendtask1.helper

import co.mvpmatch.backendtask1.web.api.model.BuyPayload
import co.mvpmatch.backendtask1.web.api.model.BuyResponseDTO
import co.mvpmatch.backendtask1.web.api.model.DepositPayload
import co.mvpmatch.backendtask1.web.api.model.ProductDTO
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.fail
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import kotlin.test.assertNotNull
import co.mvpmatch.backendtask1.web.api.model.DepositPayload.AmountInCentEnum as AmountInCentEnum

@Component
class BuyDepositHelper {
    fun addDeposit(buyMockMvc: MockMvc?, buyerToken: String, coin: AmountInCentEnum) {
        try {
            Assertions.assertNotNull(buyMockMvc)

            val payload = DepositPayload().apply {
                amountInCent = coin
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

    fun resetDeposit(buyMockMvc: MockMvc?, buyerToken: String) {
        try {
            Assertions.assertNotNull(buyMockMvc)
            buyMockMvc!!.perform(
                MockMvcRequestBuilders.get("/api/reset")
                    .header("Authorization", "Bearer $buyerToken")
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
            )
                .andExpect(MockMvcResultMatchers.status().isOk)
        } catch (e: Exception) {
            GeneralHelper.throwBestException(e)
        }
    }

    fun buyProduct(
        buyMockMvc: MockMvc?,
        buyerToken: String,
        product: ProductDTO,
        amount: Int = ProductTestHelper.testAmountToBuy
    ): BuyResponseDTO {
        try {
            Assertions.assertNotNull(buyMockMvc)

            val payload = BuyPayload().apply {
                productId = product.id
                productAmount = amount
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
