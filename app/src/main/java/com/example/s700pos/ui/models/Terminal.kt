package com.example.s700pos.ui.models

import com.stripe.stripeterminal.external.callable.ConnectionTokenCallback
import com.stripe.stripeterminal.external.callable.ConnectionTokenProvider
import com.stripe.stripeterminal.external.models.ConnectionTokenException

class Terminal {
}

class TokenProvider : ConnectionTokenProvider {
    override fun fetchConnectionToken(callback: ConnectionTokenCallback) {
        try {
            // Your backend should call /v1/terminal/connection_tokens and return the
            // JSON response from Stripe. When the request to your backend succeeds,
            // return the `secret` from the response to the SDK.
            callback.onSuccess("pst_test_YWNjdF8xS0x4dFlIS2xYaEg2UEJ3LHpjMldRQzJyWlNtVThXN0VKRWREVTcxOGVSSGVvVW0_00fxAa9wBR")
        } catch (e: Exception) {
            callback.onFailure(
                ConnectionTokenException("Failed to fetch connection token", e)
            )
        }
    }
}