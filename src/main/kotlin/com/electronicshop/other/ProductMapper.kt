package com.electronicshop.other

import com.electronicshop.dto.ProductDto
import com.electronicshop.entity.Product
import com.electronicshop.enums.CategoryEnum

class ProductMapper {

    private val randomString = RandomString()

    fun mapEntityToDto(product: Product): ProductDto{
        return ProductDto(
            id = product.id,
            name = product.name,
            price = product.price,
            inventory = product.inventory,
            category = product.categoryEnum?.name,
            productCode = product.productCode
        )
    }

    fun mapDtoToEntity(productDto: ProductDto): Product{
        return Product(
            id = productDto.id,
            name = productDto.name,
            price = productDto.price,
            inventory = productDto.inventory,
            categoryEnum = productDto.category?.let { CategoryEnum.valueOf(it) },
            carts = null,
            productCode = productDto.productCode ?: randomString.randomString()
        )
    }

}