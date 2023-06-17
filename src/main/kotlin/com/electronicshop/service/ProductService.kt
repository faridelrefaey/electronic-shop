package com.electronicshop.service

import com.electronicshop.dto.ProductDto
import com.electronicshop.entity.Product
import com.electronicshop.enums.CategoryEnum
import com.electronicshop.excpetions.IdDoesNotExistException
import com.electronicshop.excpetions.InvalidRequestBodyException
import com.electronicshop.excpetions.ProductOutOfInventoryException
import com.electronicshop.other.ProductMapper
import com.electronicshop.repository.ProductRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.Optional

@Service
@Transactional
class ProductService(
    @Autowired private val productRepository: ProductRepository
): IProductService {

    private final val productMapper: ProductMapper = ProductMapper()

    override fun getAll(): List<ProductDto> {
        val productList: List<Product> = productRepository.findAll()
        return productList.map { product -> productMapper.mapEntityToDto(product) }
    }

    override fun getById(id: Long): ProductDto {
        val optionalProduct: Optional<Product> = productRepository.findById(id)

        if(optionalProduct.isEmpty){
            throw IdDoesNotExistException("No product with the given ID was found.")
        }
        else{
            return productMapper.mapEntityToDto(optionalProduct.get())
        }
    }

    override fun insert(productDto: ProductDto): ProductDto {
        val categoryEnumValues = enumValues<CategoryEnum>()
        var categoryFound: CategoryEnum? = null
        val categoryName: String? = productDto.category

        for(category in categoryEnumValues){
            if(category.name == categoryName){
                categoryFound = category
                break
            }
        }

        if(categoryFound == null){
            throw InvalidRequestBodyException("Invalid category")
        }

        val product: Product = productRepository.save(productMapper.mapDtoToEntity(productDto))

        return productMapper.mapEntityToDto(product)
    }

    override fun deleteById(id: Long): String {
        val optionalProduct: Optional<Product> = productRepository.findById(id)
        if (optionalProduct.isEmpty) {
            throw IdDoesNotExistException("No product with the given ID was found.")
        } else {
            productRepository.deleteById(id)
            return "Product deleted successfully"
        }
    }

    override fun update(id: Long, productDto: ProductDto): ProductDto {
        val productOptional: Optional<Product> = productRepository.findById(id)
        if (productOptional.isEmpty) {
            throw IdDoesNotExistException("No product with the given ID was found.")
        }

        val product: Product = productOptional.get()

        if(productDto.name == null){
            productDto.name = product.name
        }
        if(productDto.price == null){
            productDto.price = product.price
        }
        if(productDto.inventory == null){
            productDto.inventory = product.inventory
        }
        if(productDto.category == null){
            productDto.category = product.categoryEnum?.name
        }
        if(productDto.id == null){
            productDto.id = product.id
        }
        if(productDto.productCode == null){
            productDto.productCode = product.productCode
        }

        val categoryEnumValues = enumValues<CategoryEnum>()
        var categoryFound: CategoryEnum? = null
        val categoryName: String? = productDto.category

        for(category in categoryEnumValues){
            if(category.name == categoryName){
                categoryFound = category
                break
            }
        }
        if(categoryFound == null){
            throw InvalidRequestBodyException("Invalid category")
        }

        val savedProduct: Product = productRepository.save(productMapper.mapDtoToEntity(productDto))
        return productMapper.mapEntityToDto(savedProduct)
    }

    override fun getByCategory(categoryName: String): List<ProductDto> {
        val categoryEnumValues = enumValues<CategoryEnum>()
        var categoryFound: CategoryEnum? = null

        for(category in categoryEnumValues){
            if(category.name == categoryName){
                categoryFound = category
                break
            }
        }

        if(categoryFound == null){
            throw InvalidRequestBodyException("Invalid category")
        }
        else {
            val productList: List<Product> = productRepository.getByCategory(categoryFound)
            val productListDto: List<ProductDto> = productList.map { product: Product ->
                productMapper.mapEntityToDto(product)
            }
            return productListDto
        }
    }

    override fun decreaseInventoryForProduct(productId: Long): String {
        val optionalProduct: Optional<Product> = productRepository.findById(productId)
        if(optionalProduct.isEmpty) {
            throw IdDoesNotExistException("No product with the given ID was found.")
        }

        if(optionalProduct.get().inventory == 0){
            throw ProductOutOfInventoryException("This product is out of inventory.")
        }

        val product: Product = optionalProduct.get()
        product.decreaseInventoryByOne()
        productRepository.save(product)
        return "Inventory for ${product.name} decreased by one"
    }

}