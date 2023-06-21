package com.electronicshop.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*
import jakarta.validation.constraints.NotNull

@Entity
@Table(name = "e_order")
data class Order(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    @NotNull
    val orderNumber: String? = null,
    @ManyToOne
    @JoinColumn(name = "user_id")
    @NotNull
    val user: User? = null,
    @ManyToOne
    @JoinColumn(name = "cart_id")
    @NotNull
    val cart: Cart? = null
) {
}