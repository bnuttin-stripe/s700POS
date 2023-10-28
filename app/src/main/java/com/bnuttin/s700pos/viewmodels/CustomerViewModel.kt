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
data class Customer(
    val id: String? = null,
    val name: String? = null,
    val email: String? = null,
    val payments: List<PaymentIntent>? = listOf()
)

class CustomerViewModel : ViewModel() {
    var status by mutableStateOf("")
    var customer: Customer by mutableStateOf(Customer())
    var customers: List<Customer> by mutableStateOf(listOf())

    init {
        searchCustomers("")
    }

    fun searchCustomers(email: String?) {
        status = "loading"
        viewModelScope.launch {
            try {
                customers = POSApi.customer.getCustomers(email = email ?: "")
                status = "done"
            } catch (e: IOException) {
                status = "error"
            }
        }
    }

    fun getCustomer(id: String) {
        status = "loading"
        viewModelScope.launch {
            try {
                customer = POSApi.customer.getCustomer(id)
                status = "done"
            } catch (e: IOException) {
                status = "error"
            }
        }
    }

    fun resetCustomer() {
        customer = Customer()
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