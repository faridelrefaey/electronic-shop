package com.electronicshop.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*
import jakarta.validation.constraints.NotNull

@Entity
@Table(name = "e_order")
data class Order(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long?,
    @NotNull
    val orderNumber: String?,
    @ManyToOne
    @JoinColumn(name = "user_id")
    @NotNull
    val user: User?,
    @ManyToOne
    @JoinColumn(name = "cart_id")
    @NotNull
    val cart: Cart?
) {
}