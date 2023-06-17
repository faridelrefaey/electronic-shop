package com.electronicshop.feignclients

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping


@FeignClient(name = "product-feign-client", url = "http://localhost:8000")
interface ProductFeignClient {

    @PutMapping("/product/decrease/{id}")
    fun updateProduct(@PathVariable id: Long): String
}