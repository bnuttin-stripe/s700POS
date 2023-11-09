package com.bnuttin.s700pos.viewmodels

import androidx.lifecycle.ViewModel
import com.bnuttin.s700pos.api.POSApi
import com.stripe.stripeterminal.external.callable.Cancelable
import com.stripe.stripeterminal.external.callable.ConnectionTokenCallback
import com.stripe.stripeterminal.external.callable.ConnectionTokenProvider
import com.stripe.stripeterminal.external.models.ConnectionTokenException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import java.io.IOException

@Serializable
data class ConnectionToken(
    val secret: String? = null
)

var discoveryCancelable: Cancelable? = null

class TokenProvider(private val coroutineScope: CoroutineScope) : ConnectionTokenProvider {
    override fun fetchConnectionToken(callback: ConnectionTokenCallback) {
        coroutineScope.launch {
            try {
                val token = POSApi.terminal.getConnectionToken()
                callback.onSuccess(token.secret ?: "")
            } catch (e: IOException) {
                callback.onFailure(
                    ConnectionTokenException("Failed to fetch connection token", e)
                )
            }
        }
    }
}

class TerminalViewModel : ViewModel(){

}