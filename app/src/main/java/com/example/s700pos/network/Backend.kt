package com.example.s700pos.network

import com.example.s700pos.model.Product
import com.example.s700pos.model.TestResult
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.http.GET
import java.util.concurrent.TimeUnit


private const val BASE_URL = "https://complete-transparent-oval.glitch.me"

val okHttpClient = OkHttpClient.Builder()
    .readTimeout(20, TimeUnit.SECONDS)
    .connectTimeout(20, TimeUnit.SECONDS)
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
    .baseUrl(BASE_URL)
    .client(okHttpClient)
    .build()

interface ProductApi{
    @GET("products/usd")
    suspend fun getProducts(): List<Product>

    @GET("test")
    suspend fun getTest(): TestResult
}

object POSApi {
    val retrofitService : ProductApi by lazy {
        retrofit.create(ProductApi::class.java)
    }
}