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
data class PaymentMethod(
    val type: String? = null,
    val brand: String? = null,
    val last4: String? = null
)
@Serializable
data class PaymentMetadata(
    val bopis: String? = null,
    val order: String? = null,
    val cart: String? = null,
    val channel: String? = null,
    val store: String? = null
)

@Serializable
data class Payment(
    val id: String? = null,
    val amount: Int? = null,
    val amount_received: Int? = null,
    val description: String? = null,
    val status: String? = null,
    val client_secret: String? = null,
    val metadata: PaymentMetadata? = null,
    var created: Long? = null
)
class PaymentViewModel: ViewModel() {
    var status by mutableStateOf("")
    var payment: Payment by mutableStateOf(Payment())
    var customerPayments: List<Payment> by mutableStateOf(listOf())

    init {
        searchPayments("")
    }

    fun resetCustomerPayments(){
        customerPayments = listOf()
    }

    fun searchPayments(search: String?) {
        status = "loading"
        viewModelScope.launch {
            try {
                customerPayments = POSApi.payment.searchPayments(search ?: "")
                status = "done"
            } catch (e: IOException) {
                status = "error"
            }
        }
    }

    fun getCustomerPayments(id: String){
        status = "loading"

        viewModelScope.launch{
            try {
                customerPayments = POSApi.payment.getCustomerPayments(id)
                status = "done"
            } catch (e: IOException) {
                status = "error"
            }
        }
    }

    fun getPayment(id: String) : Payment{
        status = "loading"

        viewModelScope.launch{
            try {
                payment = POSApi.payment.getPayment(id)
                status = "done"
            } catch (e: IOException) {
                status = "error"
            }
        }

        return payment
    }

    fun bopisPickedUp(id: String) : Payment{
        status = "loading"

        viewModelScope.launch{
            try {
                payment = POSApi.payment.bopisPickedUp(id)
                status = "done"
            } catch (e: IOException) {
                e.printStackTrace()
                status = "done"
            }
        }

        return payment
    }
}