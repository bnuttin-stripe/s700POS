package com.bnuttin.s700pos.api

import com.stripe.stripeterminal.external.callable.ConnectionTokenCallback
import com.stripe.stripeterminal.external.callable.ConnectionTokenProvider
import com.stripe.stripeterminal.external.models.ConnectionTokenException

class TokenProvider : ConnectionTokenProvider {
    override fun fetchConnectionToken(callback: ConnectionTokenCallback) {
        try {
            // Your backend should call /v1/terminal/connection_tokens and return the
            // JSON response from Stripe. When the request to your backend succeeds,
            // return the `secret` from the response to the SDK.
            //val secret = "pst_test_YWNjdF8xS0x4dFlIS2xYaEg2UEJ3LG1FcHdWMm4zbWFXdXFCdUpIdVlxcHpSTHBBNVNJSXE_00EzDpBsmn"
            val secret = "sk_test_51KLxtYHKlXhH6PBwy2ByjIVApqXXGuWYZhkZlSTPSBcq34bmNKsFn0kcuTX7qEmq8tt8tQfY7ujBsUAW09xtU6kC00RNmH5qeg"
            callback.onSuccess(secret)
        } catch (e: Exception) {
            callback.onFailure(
                ConnectionTokenException("Failed to fetch connection token", e)
            )
        }
    }
}