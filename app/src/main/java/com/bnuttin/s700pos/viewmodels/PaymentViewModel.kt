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
data class Payment(
    val id: String? = null,
    val amount: Int? = null,
    val amount_received: Int? = null,
    val description: String? = null,
    val status: String? = null,
    val client_secret: String? = null,
    val metadata: PaymentMetadata? = null,
    val created: Long? = null,
    val payment_method: PaymentMethod? = null
)
class PaymentViewModel: ViewModel() {
    var status by mutableStateOf("")
    var payment: Payment by mutableStateOf(Payment())
    var customerPaymentsStatus by mutableStateOf("")
    var customerPayments: List<Payment> by mutableStateOf(listOf())
    var searchPaymentsStatus by mutableStateOf("")
    var searchPayments: List<Payment> by mutableStateOf(listOf())

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