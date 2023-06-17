package com.electronicshop.service

import com.electronicshop.dto.CartDto
import com.electronicshop.entity.Cart
import com.electronicshop.entity.Product
import com.electronicshop.excpetions.EmptyCartException
import com.electronicshop.excpetions.IdDoesNotExistException
import com.electronicshop.excpetions.InvalidRequestBodyException
import com.electronicshop.other.CartMapper
import com.electronicshop.repository.CartRepository
import com.electronicshop.repository.ProductRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
@Transactional
class CartService(
    @Autowired private val cartRepository: CartRepository,
    @Autowired private val productRepository: ProductRepository
): ICartService {

    private final val cartMapper: CartMapper = CartMapper()

    override fun addProductToCart(userId: Long?, productId: Long?): CartDto {
        if (userId == null || productId == null) {
            throw InvalidRequestBodyException("A user id and a product id should be provided")
        }

        val fetchedCart: Optional<Cart> = cartRepository.getCartFromUserId(userId)

        if(fetchedCart.isEmpty){
            throw IdDoesNotExistException("No cart was found for the given user ID")
        }

        if(fetchedCart.get().id == null){
            throw IdDoesNotExistException("Bad cart")
        }

        val optionalCart: Optional<Cart> = cartRepository.findById(fetchedCart.get().id!!)
        if(optionalCart.isEmpty){
            throw IdDoesNotExistException("No cart was found with the given ID")
        }

        val cart: Cart = optionalCart.get()
        val product: Optional<Product> = productRepository.findById(productId)
        if(!product.isPresent){
            throw IdDoesNotExistException("No product with the given ID could be found")
        }

        cart.addProductToCart(product.get())
        val savedCart: Cart = cartRepository.save(cart)

        return cartMapper.mapEntityToDto(savedCart)
    }

    override fun getCartFromUserId(userId: Long): CartDto {
        val fetchedCart: Optional<Cart> = cartRepository.getCartFromUserId(userId)
        if(fetchedCart.isEmpty){
            throw IdDoesNotExistException("No cart was found for the given user ID")
        }

        return cartMapper.mapEntityToDto(fetchedCart.get())
    }

    override fun removeProductsFromCart(userId: Long): String {
        val fetchedCart: Optional<Cart> = cartRepository.getCartFromUserId(userId)
        if(fetchedCart.isEmpty){
            throw IdDoesNotExistException("No cart was found for the given user ID")
        }

        if(fetchedCart.get().products?.size == 0){
            throw EmptyCartException("This cart is already empty")
        }

        val cart: Cart = fetchedCart.get()
        cart.removeAllProducts()
        cartRepository.save(cart)
        return "Cart for userId $userId has been emptied"
    }
}