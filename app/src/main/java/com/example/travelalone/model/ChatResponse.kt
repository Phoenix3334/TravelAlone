package com.example.travelalone.model

data class ChatResponse(
    val choices: List<Choice>
)

data class Choice(
    val message: ChatMessage
)
