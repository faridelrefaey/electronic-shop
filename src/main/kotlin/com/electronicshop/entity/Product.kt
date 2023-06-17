package com.electronicshop.entity

import com.electronicshop.enums.CategoryEnum
import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*
import jakarta.validation.constraints.NotNull

@Entity
data class Product(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    @NotNull
    val name: String? = null,
    val productCode: String? = null,
    @NotNull
    val price: Double? = null,
    @NotNull
    var inventory: Int? = null,
    @Enumerated(EnumType.STRING)
    @NotNull
    val categoryEnum: CategoryEnum? = null,
    @ManyToMany(mappedBy = "products", fetch = FetchType.LAZY)
    @JsonIgnore
    var carts: MutableList<Cart>? = null
) {

    fun addCart(cart: Cart){
        if(carts.isNullOrEmpty()){
            carts = mutableListOf()
        }
        carts!!.add(cart)
    }

    fun decreaseInventoryByOne(){
        inventory = inventory?.minus(1)
    }
}