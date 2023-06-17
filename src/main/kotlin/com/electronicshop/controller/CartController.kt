package com.electronicshop.controller

import com.electronicshop.dto.CartDto
import com.electronicshop.service.ICartService
import io.swagger.v3.oas.annotations.Hidden
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/cart")
class CartController(@Autowired private val iCartService: ICartService) {

    @PostMapping("/{userId}/{productId}")
    fun addProductToCart(@PathVariable("userId") userId: Long, @PathVariable("productId") productId: Long): ResponseEntity<CartDto> {
        return ResponseEntity(iCartService.addProductToCart(userId, productId), HttpStatus.OK)
    }

    @GetMapping("{userId}")
    fun getCartForUser(@PathVariable("userId") userId: Long): ResponseEntity<CartDto> {
        return ResponseEntity(iCartService.getCartFromUserId(userId), HttpStatus.OK)
    }

    @PostMapping("/{userId}")
    @Hidden
    fun removeProductsFromCart(@PathVariable("userId") userId: Long): ResponseEntity<String> {
        return ResponseEntity(iCartService.removeProductsFromCart(userId), HttpStatus.OK)
    }
}