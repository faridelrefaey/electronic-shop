package com.electronicshop.service

import com.electronicshop.entity.Cart
import com.electronicshop.entity.Order
import com.electronicshop.entity.User
import com.electronicshop.feignclients.CartFeignClient
import com.electronicshop.feignclients.ProductFeignClient
import com.electronicshop.feignclients.UserFeignClient
import com.electronicshop.other.CartMapper
import com.electronicshop.other.RandomString
import com.electronicshop.other.UserMapper
import com.electronicshop.repository.OrderRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional

class OrderService(
    @Autowired private val orderRepository: OrderRepository,
    @Autowired private val cartFeignClient: CartFeignClient,
    @Autowired private val userFeignClient: UserFeignClient,
    @Autowired private val productFeignClient: ProductFeignClient
): IOrderService {

    private val userMapper = UserMapper()
    private val cartMapper = CartMapper()
    private val randomString = RandomString()

    override fun createOrderForUser(userId: Long): String {
        val fetchedCart: Cart = cartMapper.mapDtoToEntity(cartFeignClient.retrieveCartForUser(userId))
        val fetchedUser: User = userMapper.mapDtoToEntity(userFeignClient.getUserById(userId))

        val order: Order = Order(
            id = null,
            orderNumber = randomString.randomString(),
            user = fetchedUser,
            cart = fetchedCart
        )

        orderRepository.save(order)
        for(product in fetchedCart.products!!){
            product.id?.let { productFeignClient.updateProduct(it) }
        }
        cartFeignClient.removeProductsFromCart(userId)

        return "The order with number ${order.orderNumber} has been placed successfully"
    }

}