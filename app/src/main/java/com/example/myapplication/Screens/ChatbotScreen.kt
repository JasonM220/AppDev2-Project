package com.example.myapplication.Screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

@Composable
fun ChatbotScreen() {
    var userInput by remember { mutableStateOf("") }
    val chatMessages = remember { mutableStateListOf<Pair<String, String>>() }
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Chat history display
        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(chatMessages) { (sender, message) ->
                Text(
                    text = "$sender: $message",
                    style = TextStyle(fontSize = 16.sp, color = Color.Black)
                )
            }
        }

        // User input and send button
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            TextField(
                value = userInput,
                onValueChange = { userInput = it },
                placeholder = { Text("Ask me anything...") },
                modifier = Modifier.weight(1f)
            )
            Button(
                onClick = {
                    if (userInput.isNotBlank()) {
                        chatMessages.add("User" to userInput)
                        val input = userInput
                        userInput = ""
                        coroutineScope.launch {
                            val response = getChatbotResponse(input)
                            chatMessages.add("Chatbot" to (response ?: "No response"))
                        }
                    }
                }
            ) {
                Text("Send")
            }
        }
    }
}

// Retrofit setup and API call
interface OpenAiService {
    @Headers("Authorization: Bearer YOUR_OPENAI_API_KEY", "Content-Type: application/json")
    @POST("v1/chat/completions")
    suspend fun getChatResponse(@Body request: ChatRequest): ChatResponse
}

suspend fun getChatbotResponse(userMessage: String): String? {
    val retrofit = Retrofit.Builder()
        .baseUrl("https://api.openai.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val service = retrofit.create(OpenAiService::class.java)

    val messages = listOf(mapOf("role" to "user", "content" to userMessage))
    val request = ChatRequest(model = "gpt-3.5-turbo", messages = messages)
    return try {
        val response = service.getChatResponse(request)
        response.choices.firstOrNull()?.message?.get("content")
    } catch (e: Exception) {
        "Error: ${e.message}"
    }
}

// Data classes for request/response
data class ChatRequest(val model: String, val messages: List<Map<String, String>>)
data class ChatResponse(val choices: List<Choice>)
data class Choice(val message: Map<String, String>)
