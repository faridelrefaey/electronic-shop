package com.electronicshop.excpetions

import java.lang.RuntimeException

class InvalidRequestBodyException(message: String): RuntimeException(message){
}