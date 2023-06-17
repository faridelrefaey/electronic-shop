package com.electronicshop.excpetions

import java.lang.RuntimeException

class IdDoesNotExistException(message: String): RuntimeException(message)