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
data class PaymentIntentMetadata(
    val bopis: String? = null,
    val order: String? = null,
    val cart: String? = null,
    val channel: String? = null,
    val store: String? = null
)

@Serializable
data class PaymentIntent(
    val id: String? = null,
    val amount: Int? = null,
    val amount_received: Int? = null,
    val description: String? = null,
    val status: String? = null,
    val client_secret: String? = null,
    val metadata: PaymentIntentMetadata? = null,
    var created: Long? = null
)

class PaymentViewModel: ViewModel() {
    var status by mutableStateOf("")
    var paymentIntent: PaymentIntent by mutableStateOf(PaymentIntent()) // RENAME TO newPaymentIntent?
    var customerPayments: List<PaymentIntent> by mutableStateOf(listOf())
    // TODO rename variables

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

    fun getCustomerPayments(customerId: String){
        status = "loading"

        viewModelScope.launch{
            try {
                customerPayments = POSApi.payment.getPaymentIntents(customerId)
                status = "done"
            } catch (e: IOException) {
                status = "error"
            }
        }
    }

    fun getPaymentIntent(intentId: String) : PaymentIntent{
        status = "loading"

        viewModelScope.launch{
            try {
                paymentIntent = POSApi.payment.getPaymentIntent(intentId)
                status = "done"
            } catch (e: IOException) {
                status = "error"
            }
        }

        return paymentIntent
    }

    fun createPaymentIntent(amount: Int){
        status = "loading"
        paymentIntent = PaymentIntent(amount = amount)
        viewModelScope.launch{
            try {
                paymentIntent = POSApi.payment.createPaymentIntent(PaymentIntent(amount = amount))
                status = "done"
            } catch (e: IOException) {
                status = "error"
            }
        }
    }

    fun bopisPickedUp(intentId: String) : PaymentIntent{
        status = "loading"

        viewModelScope.launch{
            try {
                paymentIntent = POSApi.payment.bopisPickedUp(paymentIntent)
                status = "done"
            } catch (e: IOException) {
                e.printStackTrace()
                status = "done"
            }
        }

        return paymentIntent
    }
}