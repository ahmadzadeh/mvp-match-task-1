package co.mvpmatch.backendtask1.service

import co.mvpmatch.backendtask1.config.*
import co.mvpmatch.backendtask1.vm.BuyProcessPayload
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Component

@Component
@Qualifier("BuyValidationChain")
class BuyValidationChain(
    private val productService: ProductService,
    private val userService: UserService
) : BuyChain() {

    override fun process(payload: BuyProcessPayload) {
        val productId = payload.productId ?: throw InvalidParamException("productId is null")
        val buyerLogin = payload.buyerLogin ?: throw InvalidParamException("Buyer info could not be fetched")
        if (payload.amount < 0) throw InvalidParamException("Amount must be greater than zero")
        val buyer = userService.getUserByUserName(buyerLogin)
        payload.product = productService.getProductById(productId)

        //if product or buyer are null, we won't reach here
        if (payload.product!!.cost * payload.amount > buyer.deposit)
            throw InsufficientUserBalanceException()

        if (payload.product!!.amountAvailable < payload.amount)
            throw InsufficientProductAmount(payload.product!!.id)

        nextStep(payload)
    }

}
