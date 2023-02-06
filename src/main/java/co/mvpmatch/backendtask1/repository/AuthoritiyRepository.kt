package co.mvpmatch.backendtask1.repository

import co.mvpmatch.backendtask1.domain.Authority
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface AuthoritiyRepository : CrudRepository<Authority, String> {
    fun findByName(name: String): Optional<Authority>
    fun existsByName(name: String): Boolean
}
