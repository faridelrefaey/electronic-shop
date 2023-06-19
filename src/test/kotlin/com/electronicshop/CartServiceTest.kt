package com.electronicshop

import com.electronicshop.dto.CartDto
import com.electronicshop.entity.Cart
import com.electronicshop.entity.Product
import com.electronicshop.entity.User
import com.electronicshop.enums.CategoryEnum
import com.electronicshop.excpetions.EmptyCartException
import com.electronicshop.excpetions.IdDoesNotExistException
import com.electronicshop.excpetions.InvalidRequestBodyException
import com.electronicshop.other.CartMapper
import com.electronicshop.other.ProductMapper
import com.electronicshop.repository.CartRepository
import com.electronicshop.repository.ProductRepository
import com.electronicshop.service.CartService
import com.electronicshop.service.ProductService
import io.mockk.MockKAnnotations
import io.mockk.clearMocks
import io.mockk.every
import io.mockk.impl.annotations.MockK
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.boot.test.context.SpringBootTest
import java.util.*

@SpringBootTest
class CartServiceTest {

    @MockK
    private lateinit var productRepository: ProductRepository
    @MockK
    private lateinit var cartRepository: CartRepository
    private lateinit var cartService: CartService
    private lateinit var product1: Product
    private lateinit var product2: Product
    private lateinit var cart: Cart
    private lateinit var user: User
    private val cartMapper: CartMapper = CartMapper()
    private val productMapper: ProductMapper = ProductMapper()
    private var productsList: MutableList<Product> = mutableListOf()

    @BeforeEach
    fun setUp(){
        MockKAnnotations.init(this)
        cartService = CartService(cartRepository, productRepository)
        product1 = Product(
            id = 1,
            name = "Bread",
            price = 1.50,
            inventory = 100,
            categoryEnum = CategoryEnum.GROCERIES,
            productCode = "ABC",
            carts = null
        )
        product2 = Product(
            id = 2,
            name = "Food",
            price = 1.50,
            inventory = 100,
            categoryEnum = CategoryEnum.GROCERIES,
            productCode = "ABC",
            carts = null
        )
        user = User(id = 1L, name = "Farid", cart = null, orders = mutableListOf())

        productsList.add(product1)
        productsList.add(product2)
        cart = Cart(id = 1L, products = productsList, orders = mutableListOf())
    }

    @AfterEach
    fun tearDown(){
        clearMocks(productRepository, cartRepository)
    }

    @Test
    fun testAddProductToCart(){
        every { cartRepository.getCartFromUserId(1L) } returns Optional.of(cart)
        every { productRepository.findById(1L) } returns Optional.of(product1)
        every { cartRepository.save(cart) } returns cart

        val cartDto: CartDto = cartService.addProductToCart(1L, 1L)

        assertEquals(productMapper.mapEntityToDto(product1), cartDto.productsDto?.get(0))
    }

    @Test
    fun testAddProductToCartNullInputs(){
        assertThrows<InvalidRequestBodyException> { cartService.addProductToCart(null, 1L) }
        assertThrows<InvalidRequestBodyException> { cartService.addProductToCart(1L, null) }
    }

    @Test
    fun testAddProductToCartInvalidUserId(){
        every { cartRepository.getCartFromUserId(3L) } returns Optional.empty()

        assertThrows<IdDoesNotExistException> { cartService.addProductToCart(3L, 1L) }
    }

    @Test
    fun testAddProductToCartInvalidProductId(){
        every { cartRepository.getCartFromUserId(1L) } returns Optional.of(cart)
        every { productRepository.findById(3L) } returns Optional.empty()

        assertThrows<IdDoesNotExistException> { cartService.addProductToCart(1L, 3L) }
    }

    @Test
    fun testGetCartFromUserId(){
        val expectedCartDto: CartDto = cartMapper.mapEntityToDto(cart)

        every { cartRepository.getCartFromUserId(1L) } returns Optional.of(cart)

        assertEquals(expectedCartDto, cartService.getCartFromUserId(1L))
    }

    @Test
    fun testGetCartFromUserIdInvalidUserId(){
        every { cartRepository.getCartFromUserId(3L) } returns Optional.empty()

        assertThrows<IdDoesNotExistException> { cartService.getCartFromUserId(3L) }
    }

    @Test
    fun testRemoveProductsFromCart(){
        val expectedCart: Cart = Cart(1L, products = productsList, user = null, orders = mutableListOf())

        every { cartRepository.getCartFromUserId(1L) } returns Optional.of(cart)
        every { cartRepository.save(expectedCart) } returns expectedCart

        assertEquals("Cart for userId 1 has been emptied", cartService.removeProductsFromCart(1L))
        assertTrue(cartService.getCartFromUserId(1L).productsDto?.size == 0)
    }

    @Test
    fun testRemoveProductsFromCartInvalidUserId(){
        every { cartRepository.getCartFromUserId(3L) } returns Optional.empty()

        assertThrows<IdDoesNotExistException> { cartService.removeProductsFromCart(3L) }
    }

    @Test
    fun testRemoveProductsFromCartEmptyCart(){
        val expectedCart: Cart = Cart(1L, products = mutableListOf(), user = null, orders = mutableListOf())

        every { cartRepository.getCartFromUserId(1L) } returns Optional.of(expectedCart)

        assertThrows<EmptyCartException> { cartService.removeProductsFromCart(1L) }
    }
}