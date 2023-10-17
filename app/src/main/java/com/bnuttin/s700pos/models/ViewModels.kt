package com.bnuttin.s700pos.models

import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
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

@Serializable
data class Customer(
    val id: String? = null,
    val name: String? = null,
    val email: String? = null
)

@Serializable
data class PaymentIntent(
    val id: String? = null,
    val amount: Int? = null,
    val status: String? = null,
    val client_secret: String? = null
)

@Serializable
data class ConnectionToken(
    val secret: String? = null
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
    var status by mutableStateOf("")
    var customer: Customer by mutableStateOf(Customer())

    //fun searchCustomer(email: String) {
    fun searchCustomer(email: String) {
        status = "loading"
        customer = Customer()
        viewModelScope.launch {
            try {
                customer = POSApi.customer.getCustomer(email = email)
                status = "done"
            } catch (e: IOException) {
                status = "error"
            }
        }
    }

    fun updateCustomer(id: String, name: String, email: String) {
        status = "loading"
        customer = Customer()
        viewModelScope.launch {
            try {
                customer = POSApi.customer.updateCustomer(Customer(id = id, name = name, email = email))
                status = "done"
            } catch (e: IOException) {
                status = "error"
            }
        }
    }
}

class SettingsViewModel(context: Context): ViewModel() {
    var sellerName: String by mutableStateOf("Nobody")
    var statusConnectionToken by mutableStateOf("")
    var connectionToken: ConnectionToken by mutableStateOf(ConnectionToken())

    fun getConnectionToken(){
        statusConnectionToken = "loading"
        viewModelScope.launch{
            try {
                connectionToken = POSApi.terminal.getConnectionToken()
                Log.d("BENJI", connectionToken.secret ?: "None")
                statusConnectionToken = "done"
            } catch (e: IOException) {
                statusConnectionToken = "error"
            }
        }
    }

    fun updateSellerName(name: String) {
        sellerName = name
    }
}

class CheckoutViewModel: ViewModel() {
    var statusPaymentIntent by mutableStateOf("")
    var paymentIntent: PaymentIntent by mutableStateOf(PaymentIntent())

    fun createPaymentIntent(amount: Int){
        statusPaymentIntent = "loading"
        paymentIntent = PaymentIntent(amount = amount)
        viewModelScope.launch{
            try {
                paymentIntent = POSApi.payment.createPaymentIntent(PaymentIntent(amount = amount))
                statusPaymentIntent = "done"
            } catch (e: IOException) {
                statusPaymentIntent = "error"
            }
        }
    }
}