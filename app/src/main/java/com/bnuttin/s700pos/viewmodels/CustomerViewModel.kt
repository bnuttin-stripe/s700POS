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
    val id: String? = "",
    val name: String? = "",
    val email: String? = "",
    val payments: Int? = 0,
    var ltv: Double? = 0.0
)

class CustomerViewModel : ViewModel() {
    var customerLookup: Customer by mutableStateOf(Customer())
    var customerLookupStatus by mutableStateOf("")
    var customerCheckout: Customer by mutableStateOf(Customer())
    var customerCheckoutStatus by mutableStateOf("")
    var customers: List<Customer> by mutableStateOf(listOf())
    var customerCreationStatus by mutableStateOf("")

    init {
        searchCustomers("")
    }

    fun searchCustomers(search: String?) {
        customerLookupStatus = "loading"
        viewModelScope.launch {
            try {
                customers = POSApi.customer.searchCustomers(search ?: "")
                customerLookupStatus = "done"
            } catch (e: IOException) {
                customerLookupStatus = "error"
            }
        }
    }

    fun getCustomerCheckout(email: String) {
        customerCheckoutStatus = "loading"
        viewModelScope.launch {
            try {
                customerCheckout = POSApi.customer.getCustomer(email) ?: Customer()
                customerCheckoutStatus = if (customerCheckout.id == "notfound") { "not found" } else { "found" }
            } catch (e: IOException) {
                customerCheckoutStatus = "error"
            }
        }
    }

    fun getCustomer(id: String) {
        customerLookupStatus = "loading"
        viewModelScope.launch {
            try {
                customerLookup = POSApi.customer.getCustomer(id) ?: Customer()
                customerLookupStatus = "done"
            } catch (e: IOException) {
                customerLookupStatus = "error"
            }
        }
    }

    fun createCustomer(name: String, email: String) {
        customerCreationStatus = "loading"
        viewModelScope.launch {
            try {
                customerCheckout = POSApi.customer.createCustomer(Customer(name = name, email=email))
                searchCustomers("")
                customerCreationStatus = "done"
            } catch (e: IOException) {
                customerCreationStatus = "error"
            }
        }
    }

    fun resetCustomer() {
        customerLookup = Customer()
    }
}