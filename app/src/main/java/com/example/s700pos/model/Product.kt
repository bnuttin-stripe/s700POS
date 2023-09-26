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