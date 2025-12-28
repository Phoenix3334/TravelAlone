package com.example.travelalone.network

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {

    private const val BASE_URL = "https://api.deepseek.com/"

    private val okHttpClient by lazy {
        OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS) // 连接超时
            .readTimeout(60, TimeUnit.SECONDS)    // 读取超时（重点）
            .writeTimeout(60, TimeUnit.SECONDS)
            .build()
    }

    val deepSeekApi: DeepSeekApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(DeepSeekApi::class.java)
    }
}
