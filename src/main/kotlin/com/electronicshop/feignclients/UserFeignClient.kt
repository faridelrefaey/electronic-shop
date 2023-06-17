package com.electronicshop.feignclients

import com.electronicshop.dto.UserDto
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable

@FeignClient(name = "user-feign-client", url = "http://localhost:8000")
interface UserFeignClient {

    @GetMapping("/user/{id}")
    fun getUserById(@PathVariable("id") id: Long): UserDto
}