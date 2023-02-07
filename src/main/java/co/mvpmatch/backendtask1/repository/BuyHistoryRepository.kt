package co.mvpmatch.backendtask1.repository

import co.mvpmatch.backendtask1.domain.BuyHistory
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface BuyHistoryRepository : CrudRepository<BuyHistory, Long> {
}
