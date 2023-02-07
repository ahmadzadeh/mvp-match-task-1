package co.mvpmatch.backendtask1.service

import co.mvpmatch.backendtask1.config.*
import co.mvpmatch.backendtask1.domain.Product
import co.mvpmatch.backendtask1.mapper.ProductMapper
import co.mvpmatch.backendtask1.repository.ProductRepository
import co.mvpmatch.backendtask1.web.api.model.ProductDTO
import co.mvpmatch.backendtask1.web.api.model.ProductModifyPayload
import co.mvpmatch.backendtask1.web.api.model.RolesEnum
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ProductService(
    private val productRepository: ProductRepository,
    private val userService: UserService,
    private val productMapper: ProductMapper
) {

    fun getAllProducts(): List<ProductDTO> {
        return productMapper.toDTO(productRepository.findAll().toList())
    }

    @Throws(ResourceNotFoundException::class)
    fun getProductByName(productName: String): ProductDTO {
        return productMapper.toDTO(getProductEntityByName(productName))
    }

    @Throws(ResourceNotFoundException::class)
    fun getProductById(productId: Long): ProductDTO {
        return productMapper.toDTO(
            productRepository.findById(productId).orElseThrow { ResourceNotFoundException() }

        )
    }

    @Throws(ResourceAlreadyExistsException::class)
    @Transactional
    fun addProduct(
        sellerUserName: String,
        payload: ProductModifyPayload
    ) {
        if (productRepository.existsByProductName(payload.productName)) throw ResourceAlreadyExistsException()
        val entity = Product(
            seller = userService.getByUserName(sellerUserName),
            productName = payload.productName,
            amountAvailable = payload.amountAvailable,
            cost = payload.cost
        )

        productRepository.save(entity)
    }

    @Transactional
    fun updateProduct(
        editorUserName: String,
        productName: String,
        payload: ProductModifyPayload
    ) {
        val product = getProductForModification(productName, editorUserName)

        // only update non-null values in payload
        payload.productName?.let { product.productName = it }
        payload.cost?.let { product.cost = it }
        payload.amountAvailable?.let { product.amountAvailable = it }

        productRepository.save(product)
    }

    @Transactional
    fun deleteProduct(editorUserName: String, productName: String) {
        val product = getProductForModification(productName, editorUserName)
        productRepository.delete(product)
    }

    @Transactional
    @Throws(InsufficientProductAmount::class, ResourceNotFoundException::class)
    fun decreaseAmount(productId: Long, decreaseBy: Int) {
        val product = productRepository.findById(productId).orElseThrow { ResourceNotFoundException() }
        if (product.amountAvailable - decreaseBy < 0) throw  InsufficientProductAmount(productId)
        product.amountAvailable -= decreaseBy
        productRepository.save(product)
    }

    @Throws(ResourceNotFoundException::class)
    private fun getProductEntityByName(productName: String): Product {
        return productRepository.findFirstByProductName(productName)
            .orElseThrow { ResourceNotFoundException() }
    }

    private fun getProductForModification(productName: String, editorUserName: String): Product {
        val product = getProductEntityByName(productName)
        val user = userService.getUserByUserName(editorUserName)
        if (!(user.roles.contains(RolesEnum.ADMIN) || product.seller.username.equals(editorUserName, true))) {
            throw UserNotAllowedException()
        }
        return product
    }

}
