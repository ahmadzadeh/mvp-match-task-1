package co.mvpmatch.backendtask1.web.api

import co.mvpmatch.backendtask1.helper.ContextHelper
import co.mvpmatch.backendtask1.helper.BuyDepositHelper
import co.mvpmatch.backendtask1.helper.ProductTestHelper
import co.mvpmatch.backendtask1.helper.ProductTestHelper.Companion.testAmountToBuy
import co.mvpmatch.backendtask1.helper.ProductTestHelper.Companion.testProductCost
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.fail
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
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
    fun `buy process works correctly`() {
        Assertions.assertNotNull(productMockMvc)
        val sellerToken = contextHelper.getMockSeller1Token()
        productHelper.createProduct(productMockMvc!!, sellerToken)

        val buyerToken = contextHelper.getMockBuyer1Token()
        val product = productHelper.testAndGetProduct(productMockMvc!!, buyerToken)
            ?: fail("Unable to fetch product")

        //We add 200 Cents credit and buy test product with a cost of 165 cents
        buyDepositHelper.addCredit(buyMockMvc, buyerToken, AmountsInCentEnum.NUMBER_100)
        buyDepositHelper.addCredit(buyMockMvc, buyerToken, AmountsInCentEnum.NUMBER_50)
        buyDepositHelper.addCredit(buyMockMvc, buyerToken, AmountsInCentEnum.NUMBER_50)

        val buyResponse = buyDepositHelper.buyProduct(buyMockMvc, buyerToken, product)
        assertNotNull(buyResponse)
        assertEquals(buyResponse.productId, product.id)
        assertEquals(buyResponse.totalCost, testAmountToBuy * testProductCost)
        assertEquals(buyResponse.changedCoins.toSet(), setOf(20, 10, 5)) //order does not matter for set, sum must be 35
    }


}
