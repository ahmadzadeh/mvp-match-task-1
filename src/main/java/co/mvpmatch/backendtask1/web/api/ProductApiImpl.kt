package co.mvpmatch.backendtask1.web.api

import co.mvpmatch.backendtask1.config.ROLE_ADMIN
import co.mvpmatch.backendtask1.config.ROLE_SELLER
import co.mvpmatch.backendtask1.config.UnauthorizedException
import co.mvpmatch.backendtask1.config.security.getCurrentUserLogin
import co.mvpmatch.backendtask1.service.ProductService
import co.mvpmatch.backendtask1.web.api.model.ProductDTO
import co.mvpmatch.backendtask1.web.api.model.ProductModifyPayload
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.annotation.Secured
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api")
class ProductApiImpl(
    private val productService: ProductService
) : ProductApi {
    @Secured(ROLE_SELLER, ROLE_ADMIN)
    override fun _addProduct(productModifyPayload: ProductModifyPayload): ResponseEntity<ProductDTO> {
        return ResponseEntity(
            productService.addProduct(
                getCurrentUserName(),
                productModifyPayload
            ), HttpStatus.CREATED
        )
    }

    @Secured(ROLE_SELLER, ROLE_ADMIN)
    override fun _deleteProduct(productName: String): ResponseEntity<Void> {
        productService.deleteProduct(getCurrentUserName(), productName)
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build()
    }

    override fun _getAllProducts(): ResponseEntity<List<ProductDTO>> {
        return ResponseEntity.ok(productService.getAllProducts())
    }

    override fun _getProductByName(productName: String): ResponseEntity<ProductDTO> {
        return ResponseEntity.ok(productService.getProductByName(productName))
    }

    @Secured(ROLE_SELLER, ROLE_ADMIN)
    override fun _updateProduct(
        productName: String,
        productModifyPayload: ProductModifyPayload
    ): ResponseEntity<ProductDTO> {
        productService.updateProduct(getCurrentUserName(), productName, productModifyPayload)
        return ResponseEntity.ok().build()
    }

    @Throws(UnauthorizedException::class)
    private fun getCurrentUserName(): String {
        return getCurrentUserLogin() ?: throw UnauthorizedException()
    }
}
