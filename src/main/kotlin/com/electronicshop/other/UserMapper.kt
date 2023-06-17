package com.electronicshop.other

import com.electronicshop.dto.UserDto
import com.electronicshop.entity.User


class UserMapper {

    private val cartMapper = CartMapper()

    fun mapEntityToDto(user: User): UserDto{
        return UserDto(
            id = user.id,
            name = user.name,
            cartDto = user.cart?.let { cartMapper.mapEntityToDto(it) },
            orders = user.orders
         )
    }

    fun mapDtoToEntity(userDto: UserDto): User{
        return User(
            id = userDto.id,
            name = userDto.name,
            cart = userDto.cartDto?.let { cartMapper.mapDtoToEntity(it) },
            orders = userDto.orders
        )
    }
}