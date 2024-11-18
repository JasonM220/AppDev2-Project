package com.example.myapplication.Screens

import android.util.Log
import androidx.compose.foundation.layout.*
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
                            chatMessages.add("Chatbot" to response)
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
private val retrofit by lazy {
    Retrofit.Builder()
        .baseUrl("https://api.openai.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}

private val openAiService by lazy {
    retrofit.create(OpenAiService::class.java)
}

interface OpenAiService {
    @Headers(
        "Authorization: Bearer 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", // Replace with actual API key
        "Content-Type: application/json"
    )
    @POST("v1/chat/completions")
    suspend fun getChatResponse(@Body request: ChatRequest): ChatResponse
}

suspend fun getChatbotResponse(userMessage: String): String {
    val messages = listOf(ChatMessage(role = "user", content = userMessage))
    val request = ChatRequest(model = "gpt-3.5-turbo", messages = messages,
        max_tokens = 150)

    return try {
        val response = openAiService.getChatResponse(request)
        response.choices.firstOrNull()?.message?.content ?: "No response"
    } catch (e: Exception) {
        Log.e("Chatbot", "Error: ${e.message}")
        "Error: Unable to get a response"
    }
}

//BAD I KNOW, CREATED NEW DATA CLASS OUTSIDE THIS FILE

// Data classes for both the requests and responses from chat gpt
data class ChatRequest(
    val model: String,
    val messages: List<ChatMessage>,
    val max_tokens: Int
)

data class ChatMessage(
    val role: String,
    val content: String
)

data class ChatResponse(
    val choices: List<Choice>
)

data class Choice(
    val message: ChatMessage
)

