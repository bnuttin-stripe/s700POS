package com.example.s700pos.ui.screens

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.s700pos.model.Product
import com.example.s700pos.network.POSApi
import kotlinx.coroutines.launch
import java.io.IOException

data class ProductUiState(
    var status: String,
    var products: List<Product>?
)

class ProductViewModel : ViewModel() {
    var productUiState: ProductUiState by mutableStateOf(
        ProductUiState(
            status = "loading",
            products = null
        )
    )

    init {
        getProducts()
    }

    fun getProducts() {
        productUiState.status = "loading"
        viewModelScope.launch {
            productUiState = try {
                val listResult = POSApi.retrofitService.getProducts()
                ProductUiState("done", listResult)
            } catch (e: IOException) {
                ProductUiState("error", listOf())
            }
        }
    }
}
