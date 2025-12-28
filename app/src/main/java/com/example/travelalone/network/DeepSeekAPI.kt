package com.example.travelalone.network

import com.example.travelalone.model.ChatRequest
import com.example.travelalone.model.ChatResponse
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface DeepSeekApi {

    @POST("v1/chat/completions")
    suspend fun chat(
        @Header("Authorization") authorization: String,
        @Body request: ChatRequest
    ): ChatResponse
}
