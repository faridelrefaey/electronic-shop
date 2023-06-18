package com.electronicshop

import com.electronicshop.dto.ProductDto
import com.electronicshop.entity.Product
import com.electronicshop.enums.CategoryEnum
import com.electronicshop.excpetions.IdDoesNotExistException
import com.electronicshop.excpetions.InvalidRequestBodyException
import com.electronicshop.other.ProductMapper
import com.electronicshop.repository.ProductRepository
import com.electronicshop.service.ProductService
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.boot.test.context.SpringBootTest
import java.util.*

@SpringBootTest
class ProductServiceTest {

    @MockK
    private lateinit var productRepository: ProductRepository

    private lateinit var productService: ProductService

    private lateinit var product1: Product
    private lateinit var product2: Product
    private var productsList: MutableList<Product> = mutableListOf()

    private val productMapper: ProductMapper = ProductMapper()

    @BeforeEach
    fun setup() {
        MockKAnnotations.init(this)
        productService = ProductService(productRepository)

        product1 = Product(id = 1, name = "Bread", price = 1.50, inventory = 100, categoryEnum = CategoryEnum.GROCERIES, productCode = "ABC", carts = null)
        product2 = Product(id = 2, name = "Food", price = 1.50, inventory = 100, categoryEnum = CategoryEnum.GROCERIES, productCode = "ABC", carts = null)

        productsList.add(product1)
        productsList.add(product2)
    }


    @Test
    fun testGetAll(){
        every { productRepository.findAll() } returns productsList

        val productsListDto: List<ProductDto> = productsList.map { product -> productMapper.mapEntityToDto(product) }

        println(productService.getAll())

        assertEquals(productsListDto, productService.getAll())
    }

    @Test
    fun testGetById(){
        every { productRepository.findById(1L) } returns Optional.of(product1)

        val product1Dto: ProductDto = productMapper.mapEntityToDto(product1)

        assertEquals(product1Dto, productService.getById(1L))
    }

    @Test
    fun testGetByIdInvalidId(){
        every { productRepository.findById(3L) } returns Optional.empty()

        assertThrows<IdDoesNotExistException> { productService.getById(3L) }
    }

    @Test
    fun testInsert(){
        every { productRepository.save(product1) } returns product1

        val product1Dto: ProductDto = productMapper.mapEntityToDto(product1)

        assertEquals(product1Dto, productService.insert(product1Dto))
    }

    @Test
    fun testInsertWithInvalidCategory(){
        val productDto: ProductDto = ProductDto(id = 1, name = "Bread", price = 1.50, inventory = 100, category = "GR", productCode = "ABC")

        assertThrows<InvalidRequestBodyException> { productService.insert(productDto) }
    }

    @Test
    fun testDeleteById(){
        every { productRepository.findById(1L) } returns Optional.of(product1)
        every { productRepository.deleteById(1L) } returns Unit

        val expectedString: String = "Product deleted successfully"
        val actualString: String = productService.deleteById(1L)

        assertEquals(expectedString, actualString)
    }

    @Test
    fun testDeleteByIdWithInvalidId(){
        every { productRepository.findById(3L) } returns Optional.empty()

        assertThrows<IdDoesNotExistException> { productService.deleteById(3L) }
    }
}