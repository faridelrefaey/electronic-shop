package com.electronicshop.controller

import com.electronicshop.dto.ProductDto
import com.electronicshop.service.IProductService
import io.swagger.v3.oas.annotations.Hidden
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/product")
class ProductController(@Autowired private val iProductService: IProductService) {

    @GetMapping
    fun getAllProducts(@RequestParam("category") categoryName: String?): ResponseEntity<List<ProductDto>> {
        return if(categoryName == null) {
            ResponseEntity(iProductService.getAll(), HttpStatus.OK)
        } else{
            ResponseEntity(iProductService.getByCategory(categoryName), HttpStatus.OK)
        }
    }

    @GetMapping("/{id}")
    fun getById(@PathVariable id: Long): ResponseEntity<ProductDto> {
        return ResponseEntity(iProductService.getById(id), HttpStatus.OK)
    }

    @PostMapping
    fun addProduct(@RequestBody productDto: ProductDto): ResponseEntity<ProductDto> {
        return ResponseEntity(iProductService.insert(productDto), HttpStatus.CREATED)
    }

    @DeleteMapping("/{id}")
    fun deleteProduct(@PathVariable id: Long): ResponseEntity<String> {
        return ResponseEntity(iProductService.deleteById(id), HttpStatus.OK)
    }

    @PutMapping("/{id}")
    fun updateProduct(@PathVariable id: Long, @RequestBody productDto: ProductDto): ResponseEntity<ProductDto> {
        return ResponseEntity(iProductService.update(id, productDto), HttpStatus.OK)
    }

    @PutMapping("/decrease/{id}")
    @Hidden
    fun updateProduct(@PathVariable id: Long): ResponseEntity<String> {
        return ResponseEntity(iProductService.decreaseInventoryForProduct(id), HttpStatus.OK)
    }
}