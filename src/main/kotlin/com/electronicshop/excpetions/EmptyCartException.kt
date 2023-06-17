package com.electronicshop.excpetions

import java.lang.RuntimeException

class EmptyCartException(message: String): RuntimeException(message) {
}