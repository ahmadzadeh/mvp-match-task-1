package co.mvpmatch.backendtask1.repository

import co.mvpmatch.backendtask1.domain.User
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface UserRepository : CrudRepository<User, Long> {
    fun findOneByUsernameIgnoreCase(username: String): Optional<User>
    fun existsByUsernameIgnoreCase(username: String): Boolean
}
