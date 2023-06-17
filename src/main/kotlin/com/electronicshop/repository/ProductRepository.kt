package com.electronicshop.repository

import com.electronicshop.entity.Product
import com.electronicshop.enums.CategoryEnum
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
@Transactional
interface ProductRepository: JpaRepository<Product, Long> {

    @Query(value = "SELECT p FROM Product p WHERE p.categoryEnum = :categoryEnum")
    fun getByCategory(@Param("categoryEnum") categoryEnum: CategoryEnum): List<Product>
}