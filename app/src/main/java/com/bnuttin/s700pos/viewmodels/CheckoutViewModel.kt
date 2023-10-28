package com.bnuttin.s700pos.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bnuttin.s700pos.api.POSApi
import kotlinx.coroutines.launch
import java.io.IOException

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