package co.mvpmatch.backendtask1.service

import co.mvpmatch.backendtask1.config.AVAILABLE_COINS_TO_CHANGE
import co.mvpmatch.backendtask1.config.UnableToChangeCoinsException
import org.springframework.stereotype.Service

@Service
class CoinExchangeService {
    fun exchangeCoins(deposit: Int): MutableList<Int> {
        val sortedCoins = AVAILABLE_COINS_TO_CHANGE
        sortedCoins.sortDescending()
        // normally, we should not be in this situation, but if product amount is not a multiplier of 5
        // we may reach to this position, so, we throw exception to rollback the buy process
        if (deposit % 5 != 0) throw UnableToChangeCoinsException()
        val result = mutableListOf<Int>()
        var remainder = deposit
        while (remainder > 0) {
            val coin = sortedCoins.first { remainder >= it }
            result.add(coin) // get bigger amounts as much as possible
            remainder -= coin
        }
        return result
    }
}
