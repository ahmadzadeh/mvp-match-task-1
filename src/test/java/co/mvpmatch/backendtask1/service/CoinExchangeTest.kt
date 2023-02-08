package co.mvpmatch.backendtask1.service

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import kotlin.test.assertEquals

@SpringBootTest
class CoinExchangeTest {
    @Autowired
    private lateinit var coinExchangeService: CoinExchangeService

    @Test
    fun `coin exchange algorithm works`() {
        val deposit = 165
        val expectedCoins = setOf(100, 50, 10, 5)
        val coins = coinExchangeService.exchangeCoins(deposit).toSet()
        assertEquals(expectedCoins, coins)
    }
}
