package co.mvpmatch.backendtask1.web.api

import co.mvpmatch.backendtask1.helper.AuthenticationHelper
import co.mvpmatch.backendtask1.web.api.model.ProductModifyPayload
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.ObjectWriter
import com.fasterxml.jackson.databind.SerializationFeature
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
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
    private val testAmountAvailable = 10
    private val testCost = 150

    @Autowired
    private lateinit var productController: ProductApiImpl

    @Autowired
    private lateinit var authenticationHelper: AuthenticationHelper

    @BeforeEach
    fun setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(productController).build()
    }

    @Test
    fun testCreation() {
        Assertions.assertNotNull(mockMvc)

        val payload = ProductModifyPayload().apply {
            productName = testProductName
            amountAvailable = testAmountAvailable
            cost = testCost
        }

        mockMvc!!.perform(
            MockMvcRequestBuilders.post("/api/product")
                .header("Authorization", "Bearer ${authenticationHelper.getMockAdminToken()}")
                .content(serialize(payload))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
        )
            .andExpect(MockMvcResultMatchers.status().isCreated)
    }

    private fun serialize(payload: ProductModifyPayload): String {
        val mapper = ObjectMapper()
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false)
        val ow: ObjectWriter = mapper.writer().withDefaultPrettyPrinter()
        return ow.writeValueAsString(payload)
    }
}
