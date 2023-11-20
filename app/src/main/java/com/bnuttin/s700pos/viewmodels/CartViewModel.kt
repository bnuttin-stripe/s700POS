package com.bnuttin.s700pos.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class CartViewModel : ViewModel() {
    var items: List<Product> by mutableStateOf(listOf())
    var subTotal: Double by mutableDoubleStateOf(0.0)
    var tax: Double by mutableDoubleStateOf(0.0)
    var total: Double by mutableDoubleStateOf(0.0)

    fun addToCart(product: Product) {
        items += product
        subTotal += product.price
        tax = subTotal * (AppPreferences.taxPercentage?.toDouble() ?: 0.0) / 100
        //total = subTotal + tax
        total = 2005.0
    }

    fun emptyCart() {
        items = listOf()
        total = 0.0
        tax = 0.0
        subTotal = 0.0
    }
}