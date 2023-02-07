package co.mvpmatch.backendtask1.vm

import co.mvpmatch.backendtask1.web.api.model.ProductDTO

class BuyProcessPayload {
    var product: ProductDTO? = null
    var buyerLogin: String? = null
    var productId: Long? = null
    var amount: Int = 0
    var changeCoins: MutableList<Int> = mutableListOf()
}
