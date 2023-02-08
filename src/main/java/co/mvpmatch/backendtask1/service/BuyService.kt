package co.mvpmatch.backendtask1.service

import co.mvpmatch.backendtask1.config.UnexpectedException
import co.mvpmatch.backendtask1.mapper.BuyResponseMapper
import co.mvpmatch.backendtask1.web.api.model.BuyPayload
import co.mvpmatch.backendtask1.web.api.model.BuyResponseDTO
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service

@Service
class BuyService(
    @Qualifier("BuyValidationChain") private val starterChain: BuyChain,
    @Qualifier("BuyProcessorChain") private val middleChain: BuyChain,
    @Qualifier("BuyPostProcessorChain") private val finalChain: BuyChain,
    private val buyResponseMapper: BuyResponseMapper
) {
    val log = LoggerFactory.getLogger(javaClass)

    init {
        starterChain.addStep(middleChain).addStep(finalChain)
    }

    fun buy(buyerUserName: String, payload: BuyPayload): BuyResponseDTO {
        log.info("Buy process started for ${payload.productId} with amount of ${payload.productAmount}")
        starterChain.execute(buyerUserName, payload)
        val buyResult = finalChain.buyResult ?: throw UnexpectedException()
        log.info("Buy process finished for ${payload.productId} with totalCost of ${buyResult.totalCost}")
        return buyResponseMapper.toResponseDTO(buyResult)
    }
}
