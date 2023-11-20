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
import com.stripe.stripeterminal.external.callable.SetupIntentCallback
import com.stripe.stripeterminal.external.models.CaptureMethod
import com.stripe.stripeterminal.external.models.PaymentIntent
import com.stripe.stripeterminal.external.models.PaymentIntentParameters
import com.stripe.stripeterminal.external.models.SetupIntent
import com.stripe.stripeterminal.external.models.SetupIntentConfiguration
import com.stripe.stripeterminal.external.models.SetupIntentParameters
import com.stripe.stripeterminal.external.models.TerminalException
import kotlinx.coroutines.launch
import java.io.IOException
import kotlin.random.Random

class CheckoutViewModel : ViewModel() {
    var statusPaymentIntent by mutableStateOf("")
    var statusReceipt by mutableStateOf("done")
    var paymentIntendId by mutableStateOf("")
    var setupIntentPMId by mutableStateOf("")

    private fun generateOrderId(): String {
        val rand = Random.nextInt(100000, 999999)
        return (AppPreferences.orderIdPrefix ?: "") + "-" + rand.toString()
    }

    fun reset() {
        statusPaymentIntent = ""
        statusReceipt = "done"
        paymentIntendId = ""
        setupIntentPMId = ""
    }

    fun createPaymentIntent(amount: Double, items: List<Product>, customerId: String) {
        Log.d("S700POS", "Creating payment intent")
        val orderId = generateOrderId()
        val metadata = HashMap<String, String>()
        metadata["channel"] = "offline"
        metadata["store"] = AppPreferences.storeName ?: ""
        metadata["items"] = items.joinToString(separator = ", ") { it.id }
        metadata["orderId"] = orderId
        val params = PaymentIntentParameters.Builder()
            .setAmount(amount.toLong())
            .setDescription(orderId)
            .setCurrency("usd")
            .setCustomer(customerId)
            .setMetadata(metadata)
            .setCaptureMethod(CaptureMethod.Automatic)
            .build()

        Terminal.getInstance().createPaymentIntent(params, object : PaymentIntentCallback {
            override fun onSuccess(paymentIntent: PaymentIntent) {
                paymentIntendId = paymentIntent.id ?: ""
                collectPaymentMethod(paymentIntent)
            }

            override fun onFailure(e: TerminalException) {
                Log.d("S700POS", "Payment intent exception: $e")
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
                    Log.d("S700POS", "Payment method collected: $card")
                    confirmPaymentIntent(paymentIntent)
                }

                override fun onFailure(e: TerminalException) {
                    Log.d("S700POS", "Collect payment method exception $e")
                }
            })
    }

    fun confirmPaymentIntent(paymentIntent: PaymentIntent) {
        Terminal.getInstance().confirmPaymentIntent(
            paymentIntent,
            object : PaymentIntentCallback {
                override fun onSuccess(paymentIntent: PaymentIntent) {
                    Log.d("S700POS", paymentIntent.status.toString())
                    statusPaymentIntent = paymentIntent.status.toString()
                }

                override fun onFailure(e: TerminalException) {
                    Log.d("S700POS", "Confirm payment intent exception $e")
                }
            })
    }

    fun createSetupIntent() {
        val params = SetupIntentParameters.Builder()
            //.setCustomer(id)
            .build()

        Terminal.getInstance().createSetupIntent(params, object : SetupIntentCallback {
            override fun onSuccess(setupIntent: SetupIntent) {
                collectSetupIntentPaymentMethod(setupIntent)
            }

            override fun onFailure(e: TerminalException) {
                Log.d("S700POS", "Payment intent exception: $e")
            }
        })
    }

    fun collectSetupIntentPaymentMethod(setupIntent: SetupIntent) {
        val cancelable = Terminal.getInstance().collectSetupIntentPaymentMethod(
            setupIntent,
            true,
            SetupIntentConfiguration.Builder().build(),
            object : SetupIntentCallback {
                override fun onSuccess(setupIntent: SetupIntent) {
                    //Log.d("S700POS", setupIntent.toString())
                    confirmSetupIntent(setupIntent)
                }

                override fun onFailure(e: TerminalException) {
                    // Placeholder for handling exception
                }
            })
    }

    fun confirmSetupIntent(setupIntent: SetupIntent) {
        Terminal.getInstance().confirmSetupIntent(
            setupIntent,
            object : SetupIntentCallback {
                override fun onSuccess(setupIntent: SetupIntent) {
                    Log.d("S700POS", setupIntent.paymentMethodId ?: "")
                    setupIntentPMId = setupIntent.paymentMethodId ?: ""

                }

                override fun onFailure(e: TerminalException) {
                    // Placeholder for handling the exception
                }
            })
    }

    fun sendReceipt(id: String, email: String) {
        statusReceipt = "loading"
        viewModelScope.launch {
            try {
                POSApi.payment.sendReceipt(ReceiptRequest(id, email))
                statusReceipt = "done"
            } catch (e: IOException) {
                statusReceipt = "error"
            }
        }
    }

}