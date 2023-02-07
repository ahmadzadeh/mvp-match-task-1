package co.mvpmatch.backendtask1.service

import co.mvpmatch.backendtask1.vm.BuyProcessPayload
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Component

@Component
@Qualifier("BuyProcessorChain")
class BuyProcessorChain(
    private val userService: UserService,
    private val productService: ProductService
) : BuyChain() {

    override fun process(payload: BuyProcessPayload) {
        //proper null checks must be checked in previous steps.
        checkNotNull(payload.product)
        checkNotNull(payload.buyerLogin)

        val totalCost = payload.amount * payload.product!!.cost!!
        userService.decreaseCredit(payload.buyerLogin!!, totalCost)
        productService.decreaseAmount(payload.product!!.id, payload.amount)

        nextStep(payload)
    }
}
