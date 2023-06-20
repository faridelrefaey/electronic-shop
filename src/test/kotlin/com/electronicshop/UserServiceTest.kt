package com.electronicshop

import com.electronicshop.dto.UserDto
import com.electronicshop.entity.Cart
import com.electronicshop.entity.Product
import com.electronicshop.entity.User
import com.electronicshop.enums.CategoryEnum
import com.electronicshop.excpetions.IdDoesNotExistException
import com.electronicshop.excpetions.InvalidRequestBodyException
import com.electronicshop.other.UserMapper
import com.electronicshop.repository.CartRepository
import com.electronicshop.repository.UserRepository
import com.electronicshop.service.UserService
import io.mockk.MockKAnnotations
import io.mockk.clearMocks
import io.mockk.every
import io.mockk.impl.annotations.MockK
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.boot.test.context.SpringBootTest
import java.util.*

@SpringBootTest
class UserServiceTest {

    @MockK
    private lateinit var cartRepository: CartRepository
    @MockK
    private lateinit var userRepository: UserRepository
    private lateinit var userService: UserService
    private val userMapper: UserMapper = UserMapper()
    private lateinit var product1: Product
    private lateinit var product2: Product
    private lateinit var cart: Cart
    private lateinit var user: User
    private var productsList: MutableList<Product> = mutableListOf()

    @BeforeEach
    fun setUp(){
        MockKAnnotations.init(this)
        userService = UserService(userRepository, cartRepository)

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
        cart = Cart(id = 1L, products = mutableListOf(), orders = mutableListOf())
        user.cart = cart
        productsList.add(product1)
        productsList.add(product2)


    }

    @AfterEach
    fun tearDown(){
        clearMocks(cartRepository, userRepository)
    }

    @Test
    fun testInsertUser(){
        cart.id = null
        every { cartRepository.save(cart) } returns cart
        every { userRepository.save(user) } returns user

        assertEquals(userMapper.mapEntityToDto(user), userService.insertUser(userMapper.mapEntityToDto(user)))
    }

    @Test
    fun testInsertUserNullOrEmptyName(){
        assertThrows<InvalidRequestBodyException> { userService.insertUser(UserDto()) }
        assertThrows<InvalidRequestBodyException> { userService.insertUser(UserDto(name = "")) }
    }

    @Test
    fun testGetAll(){
        val user1: User = User(id = 1L, name = "Farid", cart = null, orders = mutableListOf())
        every { userRepository.findAll() } returns listOf(user, user1)

        val userDtoList: List<UserDto> = listOf(user, user1).map { user -> userMapper.mapEntityToDto(user) }
        assertEquals(userDtoList, userService.getAll())
    }

    @Test
    fun testGetUserById(){
        every { userRepository.findById(1L) } returns Optional.of(user)

        assertEquals(userMapper.mapEntityToDto(user), userService.getUserById(1L))
    }

    @Test
    fun testGetUserByIdInvalidId(){
        every { userRepository.findById(3L) } returns Optional.empty()

        assertThrows<IdDoesNotExistException> { userService.getUserById(3L) }
    }

    @Test
    fun testDeleteUserById(){
        every { userRepository.findById(1L) } returns Optional.of(user)
        every { userRepository.deleteById(1L) } returns Unit

        val expectedString: String = "The user with id 1 has been deleted successfully"
        val actualString: String = userService.deleteById(1L)
        assertEquals(expectedString, actualString)
    }

    @Test
    fun testDeleteUserByIdInvalidId(){
        every { userRepository.findById(3L) } returns Optional.empty()

        assertThrows<IdDoesNotExistException> { userService.deleteById(3L) }
    }

    @Test
    fun testGetCartIdFromUserId(){
        every { userRepository.findById(1L) } returns Optional.of(user)

        assertEquals(1L, userService.getCartIdFromUserId(1L))
    }

    @Test
    fun testGetCartIdFromUserIdInvalidUserId(){
        every { userRepository.findById(3L) } returns Optional.empty()

        assertThrows<IdDoesNotExistException> { userService.getCartIdFromUserId(3L) }
    }
}