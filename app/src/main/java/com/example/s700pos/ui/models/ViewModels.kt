package com.example.s700pos.ui.models

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.s700pos.network.POSApi
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

@Serializable
data class Customer(
    val id: String,
    val name: String,
    val email: String
)

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
                products = POSApi.product.getProducts()
                status = "done"
            } catch (e: IOException) {
                status = "error"
            }
        }
    }
}

class CartViewModel : ViewModel() {
    var items: List<Product> by mutableStateOf(listOf())
    var subTotal: Double by mutableDoubleStateOf(0.0)
    var tax: Double by mutableDoubleStateOf(0.0)
    var total: Double by mutableDoubleStateOf(0.0)

    fun addToCart(product: Product) {
        items += product
        subTotal += product.price
        tax = subTotal * 0.10
        total = subTotal + tax
    }
}

class CustomerViewModel : ViewModel() {
    var status by mutableStateOf("loading")
    var customer: List<Customer> by mutableStateOf(listOf())

    //fun searchCustomer(email: String) {
    fun searchCustomer(email: String) {
        customer = listOf()
        status = "loading"
        viewModelScope.launch {
            try {
                customer = POSApi.customer.getCustomer(email = email)
                status = "done"
            } catch (e: IOException) {
                status = "error"
            }
        }
    }

}
class SettingsViewModel: ViewModel() {
    var shop: String by mutableStateOf("HELLO")


}