package com.bnuttin.s700pos.api

import com.bnuttin.s700pos.pages.ValidationResult
import com.bnuttin.s700pos.viewmodels.AppPreferences
import com.bnuttin.s700pos.viewmodels.ConnectionToken
import com.bnuttin.s700pos.viewmodels.Customer
import com.bnuttin.s700pos.viewmodels.Payment
import com.bnuttin.s700pos.viewmodels.PaymentMethod
import com.bnuttin.s700pos.viewmodels.Product
//import com.bnuttin.s700pos.viewmodels.ValidationResult
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.Credentials
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import java.util.concurrent.TimeUnit

const val DEFAULT_URL = "https://complete-transparent-oval.glitch.me"
// TODO make a persistent fallback backend
// TODO error handling on retrofit calls

class BasicAuthInterceptor(username: String, password: String) : Interceptor {
    private var credentials: String = Credentials.basic(username, password)

    override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
        var request = chain.request()
        request = request.newBuilder().header("Authorization", credentials).build()
        return chain.proceed(request)
    }
}

// Update the URL at run time to the one set in the Settings
class URLInterceptor() : Interceptor {
    override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
        var request = chain.request()
        val newUrl =
            request.url.toString().replace(DEFAULT_URL, AppPreferences.backendUrl ?: DEFAULT_URL)
        request = request.newBuilder().url(newUrl).build()
        return chain.proceed(request)
    }
}

// Set logging on the requests
val loggingInterceptor = HttpLoggingInterceptor().apply {
    this.level = HttpLoggingInterceptor.Level.BODY
}

val okHttpClient = OkHttpClient.Builder()
    .readTimeout(20, TimeUnit.SECONDS)
    .connectTimeout(20, TimeUnit.SECONDS)
    .addInterceptor(URLInterceptor())
    .addInterceptor(loggingInterceptor)
    .build()

private var json = Json {
    ignoreUnknownKeys = true
}

private val retrofit = Retrofit.Builder()
    .addConverterFactory(
        json.asConverterFactory("application/json".toMediaType())
    )
    .baseUrl(DEFAULT_URL)
    .client(okHttpClient)
    .build()

interface SettingsApi {
    @GET("validate")
    suspend fun validateBackend(): ValidationResult
}

interface ProductApi {
    @GET("products/usd")
    suspend fun getProducts(): List<Product>
}

interface CustomerApi {
    @GET("customers/{search}")
    suspend fun searchCustomers(@Path("search") search: String): List<Customer>

    @GET("customer/{id}")
    suspend fun getCustomer(@Path("id") id: String): Customer

    @POST("customer")
    suspend fun updateCustomer(@Body customer: Customer): Customer
}

interface PaymentApi {
    @GET("payments/{search}")
    suspend fun searchPayments(@Path("search") search: String): List<Payment>

    @GET("paymentMethods/{id}")
    suspend fun getPaymentMethod(@Path("id") id: String): PaymentMethod

    @GET("payment_intents/{customerId}")
    suspend fun getCustomerPayments(@Path("customerId") id: String): List<Payment>

    @GET("payment_intent/{id}")
    suspend fun getPayment(@Path("id") id: String): Payment

    @POST("bopis-picked-up")
    suspend fun bopisPickedUp(@Body id: String): Payment
}

interface TerminalApi {
    @GET("connection_token")
    suspend fun getConnectionToken(): ConnectionToken
}

object POSApi {
    val settings: SettingsApi by lazy {
        retrofit.create(SettingsApi::class.java)
    }
    val product: ProductApi by lazy {
        retrofit.create(ProductApi::class.java)
    }
    val customer: CustomerApi by lazy {
        retrofit.create(CustomerApi::class.java)
    }
    val payment: PaymentApi by lazy {
        retrofit.create(PaymentApi::class.java)
    }
    val terminal: TerminalApi by lazy {
        retrofit.create(TerminalApi::class.java)
    }
}