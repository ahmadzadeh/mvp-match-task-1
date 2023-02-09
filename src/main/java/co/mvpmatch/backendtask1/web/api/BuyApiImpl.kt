package co.mvpmatch.backendtask1.web.api

import co.mvpmatch.backendtask1.config.ROLE_BUYER
import co.mvpmatch.backendtask1.config.UnauthorizedException
import co.mvpmatch.backendtask1.config.security.getCurrentUserLogin
import co.mvpmatch.backendtask1.service.BuyService
import co.mvpmatch.backendtask1.service.UserService
import co.mvpmatch.backendtask1.web.api.model.BuyPayload
import co.mvpmatch.backendtask1.web.api.model.BuyResponseDTO
import co.mvpmatch.backendtask1.web.api.model.DepositPayload
import org.springframework.http.ResponseEntity
import org.springframework.security.access.annotation.Secured
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api")
class BuyApiImpl(
    private val buyService: BuyService,
    private val userService: UserService
) : BuyApi, DepositApi, ResetApi {
    override fun _buy(buyPayload: BuyPayload): ResponseEntity<BuyResponseDTO> {
        return ResponseEntity.ok(buyService.buy(getCurrentUserName(), buyPayload))
    }

    @Secured(ROLE_BUYER)
    override fun _deposit(depositPayload: DepositPayload): ResponseEntity<Void> {
        userService.updateDeposit(getCurrentUserName(), depositPayload.amountInCent.value)
        return ResponseEntity.ok().build()
    }

    override fun _resetDeposits(): ResponseEntity<Void> {
        userService.resetDeposit(getCurrentUserName())
        return ResponseEntity.ok().build()
    }

    private fun getCurrentUserName(): String {
        return getCurrentUserLogin() ?: throw UnauthorizedException()
    }
}
