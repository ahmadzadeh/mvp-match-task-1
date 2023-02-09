package co.mvpmatch.backendtask1.web.api

import co.mvpmatch.backendtask1.helper.ContextHelper
import co.mvpmatch.backendtask1.helper.BuyDepositHelper
import co.mvpmatch.backendtask1.helper.ProductTestHelper
import co.mvpmatch.backendtask1.helper.ProductTestHelper.Companion.testAmountToBuy
import co.mvpmatch.backendtask1.helper.ProductTestHelper.Companion.testProductCost
import org.junit.jupiter.api.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import co.mvpmatch.backendtask1.web.api.model.DepositPayload.AmountInCentEnum as AmountInCentEnum

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class BuyApiIntTest {
    private var productMockMvc: MockMvc? = null
    private var buyMockMvc: MockMvc? = null

    @Autowired
    private lateinit var productHelper: ProductTestHelper

    @Autowired
    protected lateinit var productController: ProductApiImpl

    @Autowired
    private lateinit var buyDepositHelper: BuyDepositHelper

    @Autowired
    private lateinit var buyApiImpl: BuyApiImpl

    @Autowired
    private lateinit var contextHelper: ContextHelper

    @BeforeEach
    fun setup() {
        productMockMvc = MockMvcBuilders.standaloneSetup(productController).build()
        buyMockMvc = MockMvcBuilders.standaloneSetup(buyApiImpl).build()
        contextHelper.clearData()
    }

    @Test
    fun `deposit and buy process works correctly`() {
        Assertions.assertNotNull(productMockMvc)
        val sellerToken = contextHelper.getMockSeller1Token()
        productHelper.createProduct(productMockMvc!!, sellerToken)

        val buyerToken = contextHelper.getMockBuyer1Token()
        val product = productHelper.getProductDTO(productMockMvc!!, buyerToken)
            ?: fail("Unable to fetch product")

        //We add 200 Cents deposit and buy test product with a cost of 165 cents
        buyDepositHelper.addDeposit(buyMockMvc, buyerToken, AmountInCentEnum.NUMBER_100)
        buyDepositHelper.addDeposit(buyMockMvc, buyerToken, AmountInCentEnum.NUMBER_50)
        buyDepositHelper.addDeposit(buyMockMvc, buyerToken, AmountInCentEnum.NUMBER_50)

        val buyResponse = buyDepositHelper.buyProduct(buyMockMvc, buyerToken, product)
        assertNotNull(buyResponse)
        assertEquals(buyResponse.totalCost, testAmountToBuy * testProductCost)
        assertEquals(buyResponse.changedCoins.toSet(), setOf(20, 10, 5)) //order does not matter for set, sum must be 35
    }

    @Test
    fun `reset works correctly`() {
        Assertions.assertNotNull(productMockMvc)
        val buyerToken = contextHelper.getMockBuyer1Token()
        buyDepositHelper.resetDeposit(buyMockMvc, buyerToken)
        //TODO: get user and check credit
    }

}
