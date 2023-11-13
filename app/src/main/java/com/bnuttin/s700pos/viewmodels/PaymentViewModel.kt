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
    val id: String? = null,
    val type: String? = null,
    val card_present: PaymentMethodCP? = null,
    val card: PaymentMethodCNP? = null
)

@Serializable
data class PaymentMethodWallet(
    val type: String? = null
)

@Serializable
data class PaymentMethodCP(
    val brand: String? = null,
    val exp_month: Int? = null,
    val exp_year: Int? = null,
    val fingerprint: String? = null,
    val last4: String? = null,
    val wallet: PaymentMethodWallet? = null,
)

@Serializable
data class PaymentMethodCNP(
    val brand: String? = null,
    val exp_month: Int? = null,
    val exp_year: Int? = null,
    val fingerprint: String? = null,
    val last4: String? = null,
    val wallet: PaymentMethodWallet? = null
)

@Serializable
data class PaymentMetadata(
    val bopis: String? = null,
    val orderId: String? = null,
    val items: String? = null,
    val channel: String? = null,
    val store: String? = null
)

@Serializable
data class PaymentCharge(
    val amount: Int? = null,
    val amount_captured: Int? = null,
    val amount_refunded: Int? = null,
){
    val refunded = amount_captured == amount_refunded
}

@Serializable
data class Payment(
    val id: String? = null,
    val amount: Int? = null,
    val amount_received: Int? = null,
    val description: String? = null,
    val status: String? = null,
    val client_secret: String? = null,
    val metadata: PaymentMetadata? = null,
    val created: Long? = null,
    val payment_method: PaymentMethod? = null,
    val latest_charge: PaymentCharge? = null
)

@Serializable
data class PaymentRequest(
    val id: String
)

class PaymentViewModel: ViewModel() {
    var currentPaymentStatus by mutableStateOf("")
    var currentPayment: Payment by mutableStateOf(Payment())
    var customerPaymentsStatus by mutableStateOf("")
    var customerPayments: List<Payment> by mutableStateOf(listOf())
    var searchPaymentsStatus by mutableStateOf("")
    var searchPayments: List<Payment> by mutableStateOf(listOf())
    var bopisStatus by mutableStateOf("done")
    var refundStatus by mutableStateOf("done")


    init {
        searchPayments("")
    }

    fun resetCustomerPayments(){
        customerPayments = listOf()
    }

    fun searchPayments(search: String?) {
        searchPaymentsStatus = "loading"
        viewModelScope.launch {
            try {
                searchPayments = POSApi.payment.searchPayments(search ?: "")
                searchPaymentsStatus = "done"
            } catch (e: IOException) {
                searchPaymentsStatus = "error"
            }
        }
    }

    fun getCustomerPayments(id: String){
        customerPaymentsStatus = "loading"
        viewModelScope.launch {
            try {
                customerPayments = POSApi.payment.searchPayments("customer:'$id'")
                customerPaymentsStatus = "done"
            } catch (e: IOException) {
                customerPaymentsStatus = "error"
            }
        }
    }

    fun getPayment(id: String) : Payment{
        currentPaymentStatus = "loading"

        viewModelScope.launch{
            try {
                currentPayment = POSApi.payment.getPayment(id)
                currentPaymentStatus = "done"
            } catch (e: IOException) {
                currentPaymentStatus = "error"
            }
        }

        return currentPayment
    }

    fun bopisPickedUp(id: String) : Payment{
        bopisStatus = "loading"

        viewModelScope.launch{
            try {
                currentPayment = POSApi.payment.bopisPickedUp(PaymentRequest(id = id))
                bopisStatus = "done"
            } catch (e: IOException) {
                e.printStackTrace()
                bopisStatus = "done"
            }
        }

        return currentPayment
    }

    fun refundPayment(id: String) : Payment{
        refundStatus = "loading"

        viewModelScope.launch{
            try {
                currentPayment = POSApi.payment.refundPayment(PaymentRequest(id = id))
                refundStatus = "done"
            } catch (e: IOException) {
                e.printStackTrace()
                refundStatus = "done"
            }
        }

        return currentPayment
    }
}