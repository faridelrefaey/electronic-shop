package com.electronicshop.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
interface IOrderService {

    fun createOrderForUser(userId: Long): String
}