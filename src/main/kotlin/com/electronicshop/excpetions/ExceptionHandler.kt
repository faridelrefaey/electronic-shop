package com.electronicshop.excpetions

import com.electronicshop.excpetions.EmptyCartException
import com.electronicshop.excpetions.ErrorMessageModel
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class ExceptionHandler {

    @ExceptionHandler
    fun handleIdDoesNotExistException(ex: IdDoesNotExistException): ResponseEntity<ErrorMessageModel>{
        val error = ErrorMessageModel(HttpStatus.NOT_FOUND, HttpStatus.NOT_FOUND.value(), ex.message)
        return ResponseEntity(error, HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler
    fun handleInvalidRequestBodyException(ex: InvalidRequestBodyException): ResponseEntity<ErrorMessageModel>{
        val error = ErrorMessageModel(HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.value(), ex.message)
        return ResponseEntity(error, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler
    fun handleEmptyCartException(ex: EmptyCartException): ResponseEntity<ErrorMessageModel>{
        val error = ErrorMessageModel(HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.value(), ex.message)
        return ResponseEntity(error, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler
    fun handleProductOutOfInventoryException(ex: ProductOutOfInventoryException): ResponseEntity<ErrorMessageModel>{
        val error = ErrorMessageModel(HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.value(), ex.message)
        return ResponseEntity(error, HttpStatus.BAD_REQUEST)
    }
}