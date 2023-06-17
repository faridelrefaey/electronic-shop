package com.electronicshop.service

import com.electronicshop.dto.CartDto
import com.electronicshop.entity.Cart
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional
@Service
interface ICartService {

    fun addProductToCart(userId: Long?, productId: Long?): CartDto

    fun getCartFromUserId(userId: Long): CartDto

    fun removeProductsFromCart(userId: Long): String
}