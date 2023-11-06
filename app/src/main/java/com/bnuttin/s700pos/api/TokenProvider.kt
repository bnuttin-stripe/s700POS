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
            val secret = "pst_test_YWNjdF8xTzdQZzRGeU4wZkU5bVVILE9uSjdoeTBJUVZjTHdFSklrRG1WRGc1U3pjeXU1aWM_00QrgTvAFl"
            //val secret = "sk_test_51O7Pg4FyN0fE9mUHLdRk5UmB101F5RbxD2VYLehdsMuwLf7ynCUvm4H7V00YA63QpMK4bfTaHemdwavrVEh7SZyg00F2AD21jg"
            callback.onSuccess(secret)
        } catch (e: Exception) {
            callback.onFailure(
                ConnectionTokenException("Failed to fetch connection token", e)
            )
        }
    }
}