package com.example.Tranquility.viewmodels

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.Tranquility.Models.ChatMessage
import com.example.Tranquility.Models.ChatRequest
import com.example.Tranquility.network.openAiService
import kotlinx.coroutines.launch

class ChatbotViewModel : ViewModel() {

    val chatMessages = mutableStateListOf<Pair<String, String>>() // Holds chat history

    //context to define the ai.
    private val context = "You are an assistant chat bot for users in the app Tranquility. Your only purpose is to aid user's in meditation, stress management etc tasks, that's it."

    //adding the user input to the chat
    fun sendMessage(userMessage: String) {
        chatMessages.add("User" to userMessage)

        // Make API calls
        viewModelScope.launch {
            val response = getChatbotResponse(userMessage)
            chatMessages.add("Meditation Assistant" to response)
        }
    }

    //fetches chatbot responses
    private suspend fun getChatbotResponse(userMessage: String): String {
        val messages = listOf(
            ChatMessage(role = "system", content = context),
            ChatMessage(role = "user", content = userMessage)
        )
        val request = ChatRequest(model = "gpt-3.5-turbo", messages = messages, max_tokens = 150)

        return try {
            val response = openAiService.getChatResponse(request) //sends request
            response.choices.firstOrNull()?.message?.content ?: "No response" //return message
        } catch (e: Exception) {
            Log.e("Chatbot", "Error: ${e.message}")
            "Error: Unable to get a response"
        }
    }
}
