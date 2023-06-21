package com.electronicshop

import com.electronicshop.entity.Cart
import com.electronicshop.entity.Order
import com.electronicshop.entity.Product
import com.electronicshop.entity.User
import com.electronicshop.enums.CategoryEnum
import com.electronicshop.feignclients.CartFeignClient
import com.electronicshop.feignclients.ProductFeignClient
import com.electronicshop.feignclients.UserFeignClient
import com.electronicshop.other.CartMapper
import com.electronicshop.other.ProductMapper
import com.electronicshop.other.RandomString
import com.electronicshop.other.UserMapper
import com.electronicshop.repository.OrderRepository
import com.electronicshop.service.OrderService
import io.mockk.MockKAnnotations
import io.mockk.clearMocks
import io.mockk.every
import io.mockk.impl.annotations.MockK
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class OrderServiceTest {

    @MockK
    private lateinit var orderRepository: OrderRepository
    @MockK
    private lateinit var cartFeignClient: CartFeignClient
    @MockK
    private lateinit var userFeignClient: UserFeignClient
    @MockK
    private lateinit var productFeignClient: ProductFeignClient
    private lateinit var orderService: OrderService
    private val userMapper: UserMapper = UserMapper()
    private val cartMapper: CartMapper = CartMapper()
    private val productMapper: ProductMapper = ProductMapper()
    private lateinit var product1: Product
    private lateinit var product2: Product
    private lateinit var cart: Cart
    private lateinit var user: User
    private lateinit var order: Order
    private var productsList: MutableList<Product> = mutableListOf()

    @BeforeEach
    fun setUp(){
        MockKAnnotations.init(this)
        orderService = OrderService(orderRepository, cartFeignClient, userFeignClient, productFeignClient)
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
        productsList.add(product1)
        productsList.add(product2)
        cart = Cart(id = 1L, products = productsList, orders = mutableListOf())
        user = User(id = 1L, name = "Farid", cart = cart, orders = mutableListOf())
        order = Order(id = 1L, orderNumber = "ABC", user = user, cart = cart)
    }

    @AfterEach
    fun tearDown(){
        clearMocks(orderRepository, cartFeignClient, userFeignClient, productFeignClient)
    }

    @Test
    fun testCreateOrderForUser(){
        every { cartFeignClient.retrieveCartForUser(1L) } returns cartMapper.mapEntityToDto(cart)
        every { userFeignClient.getUserById(1L) } returns userMapper.mapEntityToDto(user)
        every { orderRepository.save(match { it is Order }) } returns order
        every { cartFeignClient.removeProductsFromCart(1L) } returns "Cart for userId 1 has been emptied"
        every { productFeignClient.decreaseProductInventory(1L) } returns "Inventory for ${product1.name} decreased by one"
        every { productFeignClient.decreaseProductInventory(2L) } returns "Inventory for ${product2.name} decreased by one"


        val expectedString: String = "The order with number"
        val actualString: String = orderService.createOrderForUser(1L)

        assertTrue(actualString.contains(expectedString))
    }

}