package com.bnuttin.s700pos.api

import android.app.Application
import android.content.Context
import com.bnuttin.s700pos.viewmodels.Customer
import com.bnuttin.s700pos.viewmodels.Payment
import com.bnuttin.s700pos.viewmodels.Product
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import java.util.concurrent.TimeUnit


private const val BASE_URL = "https://complete-transparent-oval.glitch.me"
private const val DIRECT_URL = "https://api.stripe.com/v1"

//private const val SK = "sk_test_51KLxtYHKlXhH6PBwy2ByjIVApqXXGuWYZhkZlSTPSBcq34bmNKsFn0kcuTX7qEmq8tt8tQfY7ujBsUAW09xtU6kC00RNmH5qeg"

//class BasicAuthInterceptor(username: String, password: String): Interceptor {
//    private var credentials: String = Credentials.basic(username, password)
//
//    override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
//        var request = chain.request()
//        request = request.newBuilder().header("Authorization", credentials).build()
//        return chain.proceed(request)
//    }
//}

class MyApp : Application() {

    override fun onCreate() {
        instance = this
        super.onCreate()
    }

    companion object {
        var instance: MyApp? = null
            private set

        val context: Context?
            get() = instance
    }
}

val interceptor = HttpLoggingInterceptor().apply {
    this.level = HttpLoggingInterceptor.Level.BODY
}
val okHttpClient = OkHttpClient.Builder()
    .readTimeout(20, TimeUnit.SECONDS)
    .connectTimeout(20, TimeUnit.SECONDS)
    //.addInterceptor(BasicAuthInterceptor(SK,""))
    .addInterceptor(interceptor)
    .build()

private var json = Json {
    ignoreUnknownKeys = true
}

private val retrofit = Retrofit.Builder()
    .addConverterFactory(
        json.asConverterFactory("application/json".toMediaType())
    )
    .baseUrl(BASE_URL)
    .client(okHttpClient)
    .build()

interface ProductApi {
    @GET("products/usd")
    suspend fun getProducts(): List<Product>
}

interface CustomerApi {
    @GET("customers/{search}")
    suspend fun searchCustomers(@Path("search") search: String): List<Customer>

    @GET("customer/{id}")
    suspend fun getCustomer(@Path("id") id: String) : Customer

    @POST("customer")
    suspend fun updateCustomer(@Body customer: Customer) : Customer
}

interface PaymentApi {
    @GET("payments/{search}")
    suspend fun searchPayments(@Path("search") search: String): List<Payment>

    @GET("payment_intents/{customerId}")
    suspend fun getCustomerPayments(@Path("customerId") id: String) : List<Payment>

    @GET("payment_intent/{id}")
    suspend fun getPayment(@Path("id") id: String) : Payment

    @POST("payment-intent")
    suspend fun createPaymentIntent(@Body payment: Payment) : Payment

    @POST("bopis-picked-up")
    suspend fun bopisPickedUp(@Body id: String) : Payment
}

interface TerminalApi {
    @GET("connection_token")
    suspend fun getConnectionToken() : ConnectionToken
}

object POSApi {
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