package com.electronicshop.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*
import jakarta.validation.constraints.NotNull

@Entity
@Table(name = "e_user")
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    @Column(unique = true)
    @NotNull
    val name: String? = null,
    @OneToOne(mappedBy = "user", cascade = [CascadeType.REMOVE])
    @JoinColumn(name = "cart_id")
    @NotNull
    var cart: Cart? = null,
    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    @JsonIgnore
    var orders: MutableList<Order>? = null
) {
    fun addOrders(order: Order){
        if (orders.isNullOrEmpty()){
            orders = mutableListOf()
        }
        orders!!.add(order)
    }
}