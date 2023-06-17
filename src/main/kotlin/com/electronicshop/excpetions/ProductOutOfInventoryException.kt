package com.electronicshop.excpetions

import java.lang.RuntimeException

class ProductOutOfInventoryException(message: String): RuntimeException(message) {
}