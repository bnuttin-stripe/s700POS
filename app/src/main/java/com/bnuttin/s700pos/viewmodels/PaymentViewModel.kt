package com.bnuttin.s700pos.viewmodels

import android.util.Log
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
data class PaymentIntent(
    val id: String? = null,
    val amount: Int? = null,
    val status: String? = null,
    val client_secret: String? = null
)

class PaymentViewModel: ViewModel() {
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

    // TODO change the parameter to a PaymentIntent (and add metadata to the PaymentIntent class)
    fun bopisPickedUp(id: String) {
        var paymentIntent: PaymentIntent by mutableStateOf(PaymentIntent())

        Log.d("BENJI", id)


        viewModelScope.launch{
            try {
                paymentIntent = POSApi.payment.bopisPickedUp(id)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
}