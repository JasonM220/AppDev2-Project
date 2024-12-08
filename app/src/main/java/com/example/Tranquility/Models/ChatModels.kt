package com.example.Tranquility.Models

//request body for api
data class ChatRequest(
    val model: String,
    val messages: List<ChatMessage>,
    val max_tokens: Int
)
//individual chat messages
data class ChatMessage(
    val role: String,
    val content: String
)
//response body from api
data class ChatResponse(
    val choices: List<Choice>
)
//singe response from api
data class Choice(
    val message: ChatMessage
)
