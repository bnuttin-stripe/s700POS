package com.bnuttin.s700pos.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bnuttin.s700pos.api.POSApi
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import java.io.IOException

@Serializable
data class Product(
    val id: String,
    val name: String,
    val image: String,
    val price: Double
)

class ProductViewModel() : ViewModel() {
    var status by mutableStateOf("loading")
    var products: List<Product> by mutableStateOf(listOf())

    init {
        getProducts()
    }

    fun getProducts() {
        products = listOf()
        status = "loading"
        viewModelScope.launch {
            try {
                products = POSApi.product.getProducts()
                status = "done"
            } catch (e: IOException) {
                status = "error"
            }
        }
    }
}