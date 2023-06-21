package com.electronicshop

import com.electronicshop.dto.ProductDto
import com.electronicshop.enums.CategoryEnum
import com.electronicshop.repository.ProductRepository
import com.electronicshop.service.ProductService
import com.fasterxml.jackson.databind.ObjectMapper
import org.hamcrest.Matchers.hasSize
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.TestPropertySource
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application-test.properties")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ProductControllerTest(
    @Autowired private val mockMvc: MockMvc,
    @Autowired private val productService: ProductService,
    @Autowired private val mapper: ObjectMapper,
    @Autowired private val productRepository: ProductRepository
) {

    private lateinit var productDto1: ProductDto
    private lateinit var productDto2: ProductDto
    private lateinit var productDto1Saved: ProductDto
    private lateinit var productDto2Saved: ProductDto

    @BeforeEach
    fun setUp(){
        productDto1 = ProductDto(name = "Bread", price = 1.50, inventory = 50, category = "GROCERIES")
        productDto2 = ProductDto(name = "Medicine", price = 7.25, inventory = 100, category = "HEALTH")

        productDto1Saved = productService.insert(productDto1)
        productDto2Saved = productService.insert(productDto2)
    }

    @AfterEach
    fun tearDown(){
        productService.deleteById(1L)
        productService.deleteById(2L)
    }

    @Test
    fun testGetAllProducts(){
        mockMvc.perform(MockMvcRequestBuilders.get("/product"))
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$", hasSize<Any>(2)))
            .andExpect(jsonPath("$[0].id").value(1))
            .andExpect(jsonPath("$[0].name").value("Bread"))
            .andExpect(jsonPath("$[0].category").value("GROCERIES"))
            .andExpect(jsonPath("$[1].id").value(2))
            .andExpect(jsonPath("$[1].name").value("Medicine"))
            .andExpect(jsonPath("$[1].category").value("HEALTH"))
    }

    @Test
    fun testGetProductsByCategory(){
        mockMvc.perform(MockMvcRequestBuilders.get("/product").param("category", "GROCERIES"))
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$", hasSize<Any>(1)))
            .andExpect(jsonPath("$[0].id").value(1))
            .andExpect(jsonPath("$[0].name").value("Bread"))
            .andExpect(jsonPath("$[0].category").value("GROCERIES"))
    }

    @Test
    fun testGetProductsByCategoryInvalidCategory(){
        mockMvc.perform(MockMvcRequestBuilders.get("/product").param("category", "GR"))
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.httpStatus").value("BAD_REQUEST"))
            .andExpect(jsonPath("$.status").value(400))
            .andExpect(jsonPath("$.message").value("Invalid category"))
    }

    @Test
    fun testGetProductById(){
        mockMvc.perform(MockMvcRequestBuilders.get("/product/{id}", 1))
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.name").value("Bread"))
            .andExpect(jsonPath("$.category").value("GROCERIES"))
    }

    @Test
    fun testGetByProductIdInvalidId(){
        mockMvc.perform(MockMvcRequestBuilders.get("/product/{id}", 3L))
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound)
            .andExpect(jsonPath("$.httpStatus").value("NOT_FOUND"))
            .andExpect(jsonPath("$.status").value(404))
            .andExpect(jsonPath("$.message").value("No product with the given ID was found."))
    }

    @Test
    fun testAddProduct(){
        val productDto = ProductDto(name = "Book", price = 7.25, inventory = 100, category = "EDUCATION")
        mockMvc.perform(MockMvcRequestBuilders.post("/product")
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(productDto)))
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated)
            .andExpect(jsonPath("$.id").value(3))
            .andExpect(jsonPath("$.name").value("Book"))
            .andExpect(jsonPath("$.category").value("EDUCATION"))
    }

    @Test
    fun testDeleteProduct(){
        val productDto = ProductDto(name = "Book", price = 7.25, inventory = 100, category = "EDUCATION")
        productService.insert(productDto)
        mockMvc.perform(MockMvcRequestBuilders.delete("/product/{id}", 3L))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$").value("Product deleted successfully"))
    }

    @Test
    fun testDeleteProductInvalidProductId(){
        mockMvc.perform(MockMvcRequestBuilders.delete("/product/{id}", 3L))
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound)
            .andExpect(jsonPath("$.httpStatus").value("NOT_FOUND"))
            .andExpect(jsonPath("$.status").value(404))
            .andExpect(jsonPath("$.message").value("No product with the given ID was found."))
    }

    @Test
    fun testUpdateProduct(){
        val productDto = ProductDto(inventory = 150)

        mockMvc.perform(MockMvcRequestBuilders.put("/product/{id}",1L)
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(productDto)))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.name").value("Bread"))
            .andExpect(jsonPath("$.category").value("GROCERIES"))
            .andExpect(jsonPath("$.inventory").value(150))
    }

    @Test
    fun testUpdateProductInvalidId(){
        val productDto = ProductDto(inventory = 150)

        mockMvc.perform(MockMvcRequestBuilders.put("/product/{id}",3L)
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(productDto)))
            .andExpect(status().isNotFound)
            .andExpect(jsonPath("$.httpStatus").value("NOT_FOUND"))
            .andExpect(jsonPath("$.status").value(404))
            .andExpect(jsonPath("$.message").value("No product with the given ID was found."))
    }

    @Test
    fun testUpdateProductInvalidCategory(){
        val productDto = ProductDto(category = "a")

        mockMvc.perform(MockMvcRequestBuilders.put("/product/{id}",1L)
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(productDto)))
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.httpStatus").value("BAD_REQUEST"))
            .andExpect(jsonPath("$.status").value(400))
            .andExpect(jsonPath("$.message").value("Invalid category"))
    }
}