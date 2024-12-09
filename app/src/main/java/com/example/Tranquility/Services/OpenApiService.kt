package com.example.Tranquility.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import com.example.Tranquility.Models.ChatRequest
import com.example.Tranquility.Models.ChatResponse

// initializes the Retrofit instance, which sets up HTTP communication
private val retrofit by lazy {
    Retrofit.Builder()
        .baseUrl("https://api.openai.com/") //base url for api
        .addConverterFactory(GsonConverterFactory.create())
        .build() //builds retrofit instance
}

// Lazily initializes the API service for api calls
val openAiService: OpenAiService by lazy {
    retrofit.create(OpenAiService::class.java)
}

// Defines the API endpoints and headers for interacting with OpenAI
interface OpenAiService {
    @Headers(
        "Authorization: Bearer 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",
        "Content-Type: application/json" //fomat
    )
    @POST("v1/chat/completions") //endpoint for sending
    suspend fun getChatResponse(@Body request: ChatRequest): ChatResponse
}
