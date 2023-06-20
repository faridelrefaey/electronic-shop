package com.electronicshop.repository

import com.electronicshop.entity.Cart
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Repository
@Transactional
interface CartRepository: JpaRepository<Cart, Long> {

    //@Query(value = "SELECT c FROM Cart c JOIN c.user u WHERE u.id = :userId")
    @Query(value = "SELECT u.cart FROM User u WHERE u.id = :userId ")
    fun getCartFromUserId(@Param("userId") userId: Long): Optional<Cart>
}