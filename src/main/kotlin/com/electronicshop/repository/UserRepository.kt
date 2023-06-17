package com.electronicshop.repository

import com.electronicshop.entity.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository: JpaRepository<User, Long> {
}