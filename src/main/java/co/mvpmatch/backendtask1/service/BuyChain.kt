package co.mvpmatch.backendtask1.service

import co.mvpmatch.backendtask1.vm.BuyProcessPayload
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
// it is important that buy chain runs in transactional.
// having @transactional at super class, ensure that sub-classes always are in the same transactional context
// of course, different propagation settings should not be used to have different transactions in sub-classes
@Transactional
abstract class BuyChain {
    private var next: BuyChain? = null

    fun addStep(next: BuyChain): BuyChain {
        this.next = next
        return next
    }

    abstract fun process(payload: BuyProcessPayload)

    protected fun nextStep(payload: BuyProcessPayload) {
        next?.process(payload)
    }

}
