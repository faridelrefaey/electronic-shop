package com.electronicshop.dto

import com.electronicshop.entity.Cart
import com.electronicshop.entity.Order
import com.electronicshop.entity.Product
import com.electronicshop.entity.User
import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.FetchType
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToOne
import org.jetbrains.annotations.NotNull

data class CartDto(
    val id: Long? = null,
    var productsDto: MutableList<ProductDto>? = null,
    @JsonIgnore
    val userDto: User? = null,
    @JsonIgnore
    val orders: MutableList<Order>? = null
)