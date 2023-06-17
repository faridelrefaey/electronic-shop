package com.electronicshop.excpetions

import org.springframework.http.HttpStatus

class ErrorMessageModel (
    var httpStatus: HttpStatus? = null,
    var status: Int? = null,
    var message: String? = null
)
