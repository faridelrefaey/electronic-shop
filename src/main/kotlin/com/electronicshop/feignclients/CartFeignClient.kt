package com.electronicshop.feignclients

import com.electronicshop.dto.CartDto
import com.electronicshop.entity.Cart
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping

@FeignClient(name="cart-feign-client", url = "http://localhost:8000")
interface CartFeignClient {

    @GetMapping("/cart/{userId}")
    fun retrieveCartForUser(@PathVariable("userId") userId: Long): CartDto

    @PostMapping("/cart/{userId}")
    fun removeProductsFromCart(@PathVariable("userId") userId: Long): String
}