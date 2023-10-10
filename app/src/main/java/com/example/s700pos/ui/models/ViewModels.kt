package com.example.s700pos.ui.models

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.s700pos.model.Product
import com.example.s700pos.network.POSApi
import kotlinx.coroutines.launch
import java.io.IOException

class ProductViewModel : ViewModel() {
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
                products = POSApi.retrofitService.getProducts()
                status = "done"
            } catch (e: IOException) {
                status = "error"
            }
        }
    }
}

class CartViewModel : ViewModel() {
    var items: List<Product> by mutableStateOf(listOf())

    fun addToCart(product: Product) {
        items += product
    }
}
