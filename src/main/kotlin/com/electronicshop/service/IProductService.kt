package com.electronicshop.service

import com.electronicshop.dto.ProductDto
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
interface IProductService {

    fun getAll(): List<ProductDto>

    fun getById(id: Long): ProductDto

    fun insert(productDto: ProductDto): ProductDto

    fun deleteById(id: Long): String

    fun update(id: Long, productDto: ProductDto): ProductDto

    fun getByCategory(categoryName: String): List<ProductDto>

    fun decreaseInventoryForProduct(productId: Long): String
}