package com.example.travelalone.model

data class ChatRequest(
    val model: String = "deepseek-chat",
    val messages: List<ChatMessage>,
    val temperature: Double = 0.7,
    val max_tokens: Int = 1000,
    val stream: Boolean = false
)
