package com.bnuttin.s700pos.api

import com.stripe.stripeterminal.external.callable.ConnectionTokenCallback
import com.stripe.stripeterminal.external.callable.ConnectionTokenProvider
import com.stripe.stripeterminal.external.models.ConnectionTokenException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.io.IOException

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
