package com.electronicshop.dto

data class ProductDto(
    var id: Long? = null,
    var name: String? = null,
    var price: Double? = null,
    var inventory: Int? = null,
    var category: String? = null,
    var productCode: String? = null
    )