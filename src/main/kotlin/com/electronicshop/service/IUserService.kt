package com.electronicshop.service

import com.electronicshop.dto.UserDto
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
interface IUserService {

    fun insertUser(userDto: UserDto): UserDto

    fun getAll(): List<UserDto>

    fun getUserById(id: Long): UserDto

    fun deleteById(id: Long): String

    fun getCartIdFromUserId(userId: Long): Long?


}