package com.bnuttin.s700pos.viewmodels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bnuttin.s700pos.api.POSApi
import com.stripe.stripeterminal.Terminal
import com.stripe.stripeterminal.external.callable.PaymentIntentCallback
import com.stripe.stripeterminal.external.models.CaptureMethod
import com.stripe.stripeterminal.external.models.PaymentIntent
import com.stripe.stripeterminal.external.models.PaymentIntentParameters
import com.stripe.stripeterminal.external.models.TerminalException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.IOException

class CheckoutViewModel : ViewModel() {
    var statusPaymentIntent by mutableStateOf("")
    private val _currentPaymentIntent =
        MutableStateFlow<com.stripe.stripeterminal.external.models.PaymentIntent?>(null)
    val currentPaymentIntent = _currentPaymentIntent.asStateFlow()
    var currentPayment: Payment by mutableStateOf(Payment())

    fun createPaymentIntent(amount: Int) {
        statusPaymentIntent = "loading"
        viewModelScope.launch {
            try {
                currentPayment = POSApi.payment.createPaymentIntent(Payment(amount = amount))
                //_currentPaymentIntent.update{ POSApi.payment.createPaymentIntent(amount = amount) }
                statusPaymentIntent = "done"
            } catch (e: IOException) {
                statusPaymentIntent = "error"
            }
        }
    }

    fun createPaymentIntentNew(amount: Long) {
        Log.d("BENJI", "Creating payment intent")
        val params = PaymentIntentParameters.Builder()
            .setAmount(amount/100)
            .setCurrency("usd")
            .setCaptureMethod(CaptureMethod.Automatic)
            .build()
        Terminal.getInstance().createPaymentIntent(params, object : PaymentIntentCallback {
            override fun onSuccess(paymentIntent: PaymentIntent) {
                // Placeholder for collecting a payment method with paymentIntent
                Log.d("BENJI", "Payment intent created $paymentIntent")
                collectPaymentMethod(paymentIntent)
            }

            override fun onFailure(exception: TerminalException) {
                // Placeholder for handling exception
                Log.d("BENJI", "Payment intent exception: $exception")
            }
        })
    }

    fun collectPaymentMethod(paymentIntent: PaymentIntent) {
        val cancelable = Terminal.getInstance().collectPaymentMethod(
            paymentIntent,
            object : PaymentIntentCallback {
                override fun onSuccess(paymentIntent: PaymentIntent) {
                    val pm = paymentIntent.paymentMethod
                    val card = pm?.cardPresentDetails ?: pm?.interacPresentDetails
                    Log.d("BENJI", "Payment method collected: $card")
                    confirmPaymentIntent(paymentIntent)
                }

                override fun onFailure(exception: TerminalException) {
                    // Placeholder for handling exception
                }
            })
    }

    fun confirmPaymentIntent(paymentIntent: PaymentIntent) {
        Terminal.getInstance().confirmPaymentIntent(paymentIntent, object : PaymentIntentCallback {
            override fun onSuccess(paymentIntent: PaymentIntent) {

            }

            override fun onFailure(exception: TerminalException) {
                // Placeholder for handling the exception
            }
        })
    }
}