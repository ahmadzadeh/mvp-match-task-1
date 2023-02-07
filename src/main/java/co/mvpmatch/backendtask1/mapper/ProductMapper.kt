package co.mvpmatch.backendtask1.mapper

import co.mvpmatch.backendtask1.domain.Product
import co.mvpmatch.backendtask1.web.api.model.ProductDTO
import co.mvpmatch.backendtask1.web.api.model.ProductModifyPayload
import org.mapstruct.Mapper

@Mapper(componentModel = "spring", uses = [])
abstract class ProductMapper {
    abstract fun toDTO(entity: Product): ProductDTO
    abstract fun toDTO(entities: List<Product>): List<ProductDTO>
}
