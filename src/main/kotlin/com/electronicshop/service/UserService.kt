package com.electronicshop.service

import com.electronicshop.dto.UserDto
import com.electronicshop.entity.Cart
import com.electronicshop.entity.User
import com.electronicshop.excpetions.IdDoesNotExistException
import com.electronicshop.excpetions.InvalidRequestBodyException
import com.electronicshop.other.UserMapper
import com.electronicshop.repository.CartRepository
import com.electronicshop.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
@Transactional
class UserService(
    @Autowired private val userRepository: UserRepository,
    @Autowired private val cartRepository: CartRepository
): IUserService {

    private val userMapper = UserMapper()

    override fun insertUser(userDto: UserDto): UserDto {
        if (userDto.name.isNullOrEmpty()) {
            throw InvalidRequestBodyException("User object provided is invalid")
        }

        val user: User = userMapper.mapDtoToEntity(userDto)
        user.cart = cartRepository.save(Cart(id = null, products = mutableListOf(), user = user, orders = null))
        val savedUser: User = userRepository.save(user)
        return userMapper.mapEntityToDto(savedUser)
    }

    override fun getAll(): List<UserDto> {
        return userRepository.findAll().map { user -> userMapper.mapEntityToDto(user) }
    }

    override fun getUserById(id: Long): UserDto {
        val optionalUser: Optional<User> = userRepository.findById(id)
        if (optionalUser.isEmpty) {
            throw IdDoesNotExistException("No user could be found with the given ID")
        }

        return userMapper.mapEntityToDto(optionalUser.get())
    }

    override fun deleteById(id: Long): String {
        val optionalUser: Optional<User> = userRepository.findById(id)
        if (optionalUser.isEmpty) {
            throw IdDoesNotExistException("No user could be found with the given ID")
        }

        userRepository.deleteById(id)
        return "The user with id $id has been deleted successfully"
    }

    override fun getCartIdFromUserId(userId: Long): Long? {
        val optionalUser: Optional<User> = userRepository.findById(userId)
        if (optionalUser.isEmpty) {
            throw IdDoesNotExistException("No user could be found with the given ID")
        }
        return optionalUser.get().cart?.id
    }
}