package com.example.travelalone.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.travelalone.model.ChatMessage
import com.example.travelalone.model.ChatRequest
import com.example.travelalone.network.RetrofitClient
import kotlinx.coroutines.launch

class ChatViewModel : ViewModel() {

    companion object {
        // ⚠️ 仅学习演示
        private const val API_KEY = "sk-0cd9eb70da6d4cb99b9444affb708819"
    }

    private val _messages = MutableLiveData<List<ChatMessage>>(listOf(
        ChatMessage(
            role = "system",
            content = "你叫“城市信息查询小助手”，我会问你一些有关于城市信息，旅游信息，或者其他的相关信息。请你简要回答即可。"
        )
    ))
    val messages: LiveData<List<ChatMessage>> = _messages

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String?>(null)
    val error: LiveData<String?> = _error

    fun sendMessage(userInput: String) {
        if (userInput.isBlank()) return

        val currentMessages = _messages.value.orEmpty()
        val updatedMessages = currentMessages + ChatMessage(
            role = "user",
            content = userInput
        )
        _messages.value = updatedMessages

        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            try {
                val request = ChatRequest(
                    messages = updatedMessages
                )
                Log.d("ChatViewModel", "[DEBUG] 请求体: $request")
                val response = RetrofitClient.deepSeekApi.chat(
                    authorization = "Bearer $API_KEY",
                    request = request
                )
                Log.d("ChatViewModel", "[DEBUG] 响应体: $response")
                val newMessage = response.choices.firstOrNull()?.message
                if (newMessage != null) {
                    _messages.value = _messages.value.orEmpty() + newMessage
                } else {
                    _error.value = "AI未返回有效内容"
                    Log.d("ChatViewModel", "[DEBUG] AI未返回有效内容")
                }
            } catch (e: Exception) {
                _error.value = "请求失败，请稍后再试"
                Log.d("ChatViewModel", "[DEBUG] 异常: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }
}
