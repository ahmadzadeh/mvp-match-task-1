package co.mvpmatch.backendtask1.service

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class CoinExchangeTest {
    @Test
    fun `coin exchange algorithm works`() {
        val coinExchangeService = CoinExchangeService()
        val deposit = 165
        val expectedCoins = setOf(100, 50, 10, 5)
        val coins = coinExchangeService.exchangeCoins(deposit).toSet()
        assertEquals(expectedCoins, coins)
    }
}
