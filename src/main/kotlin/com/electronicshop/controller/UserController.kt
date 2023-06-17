package com.electronicshop.controller

import com.electronicshop.dto.UserDto
import com.electronicshop.service.IUserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/user")
class UserController(@Autowired private val iUserService: IUserService) {

    @PostMapping
    fun addUser(@RequestBody userDto: UserDto): ResponseEntity<UserDto> {
        return ResponseEntity(iUserService.insertUser(userDto), HttpStatus.CREATED)
    }

    @GetMapping
    fun getAllUsers(): ResponseEntity<List<UserDto>> {
        return ResponseEntity(iUserService.getAll(), HttpStatus.OK)
    }

    @GetMapping("/{id}")
    fun getUserById(@PathVariable("id") id: Long): ResponseEntity<UserDto> {
        return ResponseEntity(iUserService.getUserById(id), HttpStatus.OK)
    }

    @DeleteMapping("{id}")
    fun deleteUserById(@PathVariable("id") id: Long): ResponseEntity<String> {
        return ResponseEntity(iUserService.deleteById(id), HttpStatus.OK)
    }
}