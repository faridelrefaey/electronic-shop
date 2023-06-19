package com.electronicshop.other

import com.electronicshop.dto.CartDto
import com.electronicshop.entity.Cart

class CartMapper {

    private val productMapper: ProductMapper = ProductMapper()


    fun mapEntityToDto(cart: Cart): CartDto {
        return CartDto(
            id = cart.id,
            productsDto = cart.products?.map { product ->  productMapper.mapEntityToDto(product)}?.toMutableList(),
            userDto = cart.user,
            orders = cart.orders
        )
    }

    fun mapDtoToEntity(cartDto: CartDto): Cart{
        return Cart(
            id = cartDto.id,
            products = cartDto.productsDto?.map { productDto ->  productMapper.mapDtoToEntity(productDto)}?.toMutableList(),
            user = cartDto.userDto,
            orders = cartDto.orders
        )
    }

}
