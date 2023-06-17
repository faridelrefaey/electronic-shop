package com.electronicshop.dto

import com.electronicshop.entity.Order
import com.fasterxml.jackson.annotation.JsonIgnore

data class UserDto(
    val id: Long? = null,
    val name: String? = null,
    var cartDto: CartDto? = null,
    @JsonIgnore
    var orders: MutableList<Order>? = null
)