package com.electronicshop.service

import com.electronicshop.entity.Cart
import com.electronicshop.entity.Order
import com.electronicshop.entity.User
import com.electronicshop.excpetions.EmptyCartException
import com.electronicshop.feignclients.CartFeignClient
import com.electronicshop.feignclients.ProductFeignClient
import com.electronicshop.feignclients.UserFeignClient
import com.electronicshop.other.CartMapper
import com.electronicshop.other.RandomString
import com.electronicshop.other.UserMapper
import com.electronicshop.repository.OrderRepository
import feign.FeignException
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
        var fetchedCart: Cart? = null
        var fetchedUser: User? = null
        try {
            fetchedCart = cartMapper.mapDtoToEntity(cartFeignClient.retrieveCartForUser(userId))
        }
        catch (exception: FeignException.NotFound){
            return exception.contentUTF8()
        }

        try {
            fetchedUser = userMapper.mapDtoToEntity(userFeignClient.getUserById(userId))
        }
        catch (exception: FeignException.NotFound){
            return exception.contentUTF8()
        }


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
        try {
            cartFeignClient.removeProductsFromCart(userId)
        }
        catch (exception: FeignException.BadRequest){
                return exception.contentUTF8()
        }

        return "The order with number ${order.orderNumber} has been placed successfully"
    }

}