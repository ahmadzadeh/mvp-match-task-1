package co.mvpmatch.backendtask1.web.api

import co.mvpmatch.backendtask1.helper.ContextHelper
import co.mvpmatch.backendtask1.helper.GeneralHelper
import co.mvpmatch.backendtask1.helper.ProductTestHelper
import co.mvpmatch.backendtask1.web.api.model.RegistrationDTO
import co.mvpmatch.backendtask1.web.api.model.RolesEnum
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import kotlin.test.assertNotNull

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserApiIntTest {
    private var mockMvc: MockMvc? = null

    @Autowired
    private var userApiImpl: UserApiImpl? = null

    @Autowired
    private lateinit var contextHelper: ContextHelper

    @BeforeEach
    fun setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(userApiImpl).build()
        contextHelper.clearData()
    }

    @Test
    fun `registration works`() {
        try {
            val payload = RegistrationDTO().apply {
                username = "Reg_Username"
                fullName = "Registered Buyer"
                password = "Password"
                roles = listOf(RolesEnum.SELLER)
            }
            Assertions.assertNotNull(mockMvc)
            mockMvc!!.perform(
                MockMvcRequestBuilders.post("/api/user")
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(GeneralHelper.serializeJson(payload))
            )
                .andExpect(MockMvcResultMatchers.status().isCreated)

            assertNotNull(contextHelper.getAccessToken(payload.username, payload.password))
        } catch (e: Exception) {
            GeneralHelper.throwBestException(e)
        }
    }

}
