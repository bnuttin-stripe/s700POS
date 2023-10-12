package com.example.s700pos.network

import com.example.s700pos.model.TestResult
import com.example.s700pos.ui.models.Customer
import com.example.s700pos.ui.models.Product
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.http.GET
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

val okHttpClient = OkHttpClient.Builder()
    .readTimeout(20, TimeUnit.SECONDS)
    .connectTimeout(20, TimeUnit.SECONDS)
    //.addInterceptor(BasicAuthInterceptor(SK,""))
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

    @GET("test")
    suspend fun getTest(): TestResult
}

interface CustomerApi {
    //@GET("customer/\$email")
    @GET("customer/{email}")
    suspend fun getCustomer(@Path("email") email: String): List<Customer>

    @GET("test")
    suspend fun getTest(): TestResult
}

object POSApi {
    val product: ProductApi by lazy {
        retrofit.create(ProductApi::class.java)
    }
    val customer: CustomerApi by lazy {
        retrofit.create(CustomerApi::class.java)
    }
}