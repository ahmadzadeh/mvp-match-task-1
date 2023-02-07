package co.mvpmatch.backendtask1.service

import co.mvpmatch.backendtask1.domain.BuyHistory
import co.mvpmatch.backendtask1.repository.BuyHistoryRepository
import co.mvpmatch.backendtask1.vm.BuyProcessPayload
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Component

@Component
@Qualifier("BuyProcessorChain")
class BuyPostProcessorChain(
    private val userService: UserService,
    private val exchangeService: CoinExchangeService,
    private val buyHistoryRepository: BuyHistoryRepository
) : BuyChain() {
    override fun process(payload: BuyProcessPayload) {
        //proper null checks must be checked in previous steps.
        checkNotNull(payload.buyerLogin)

        val buyer = userService.getUserByUserName(payload.buyerLogin!!) // fetch updated credit from DB
        payload.changeCoins = if (buyer.credit > 0) exchangeService.exchangeCoins(buyer.credit) else mutableListOf()
        buyHistoryRepository.save(
            BuyHistory(
                productId = payload.productId!!,
                buyerId = buyer.id
            )
        )

        nextStep(payload)
    }


}
