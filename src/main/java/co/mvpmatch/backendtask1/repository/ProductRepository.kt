package co.mvpmatch.backendtask1.repository

import co.mvpmatch.backendtask1.domain.Product
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface ProductRepository : CrudRepository<Product, Long> {
    fun findFirstByProductName(productName: String): Optional<Product>
    fun existsByProductName(productName: String): Boolean
}
