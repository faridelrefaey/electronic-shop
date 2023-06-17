package com.electronicshop.controller

import com.electronicshop.service.IOrderService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/order")
class OrderController(@Autowired private val iOrderService: IOrderService) {

    @PostMapping("{userId}")
    fun createOrder(@PathVariable("userId") userId: Long): ResponseEntity<String> {
        return ResponseEntity(iOrderService.createOrderForUser(userId), HttpStatus.CREATED)
    }
}