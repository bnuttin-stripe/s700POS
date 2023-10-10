package com.example.s700pos.model

import kotlinx.serialization.Serializable

@Serializable
data class Product(
    val id: String,
    val name: String,
    val image: String,
    val price: Int
)

@Serializable
data class TestResult(
    val message: String
)
@Serializable
data class StripePrice(
    val id: String,
    val unit_amount: Int
)
@Serializable
data class StripeProduct(
    val id: String,
    val name: String,
    val images: List<String>,
    val default_price: StripePrice
)

@Serializable
data class ProductResponse(
    val data: List<StripeProduct>,
    val has_more: Boolean
)