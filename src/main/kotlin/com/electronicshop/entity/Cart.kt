package com.electronicshop.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*
import org.jetbrains.annotations.NotNull

@Entity
data class Cart(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "cart_product",
        joinColumns = [JoinColumn(name = "cart_id")],
        inverseJoinColumns = [JoinColumn(name = "product_id")]
    )
    var products: MutableList<Product>? = null,

//    @OneToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "user_id")
//    @NotNull
//    @JsonIgnore
//    val user: User? = null,

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "cart",cascade = [CascadeType.ALL])
    @JsonIgnore
    val orders: MutableList<Order>? = null
) {

    fun addProductToCart(product: Product){
        if(products.isNullOrEmpty()){
            products = mutableListOf()
        }
        products!!.add(product)
    }

    fun removeAllProducts(){
        products!!.clear()
    }
}