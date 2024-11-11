package com.example.catalog.content.data.repositories

import android.graphics.Bitmap
import com.google.firebase.Firebase
import com.google.firebase.vertexai.GenerativeModel
import com.google.firebase.vertexai.type.GenerateContentResponse
import com.google.firebase.vertexai.type.content
import com.google.firebase.vertexai.vertexAI
import javax.inject.Inject

class GeminiAiRepository @Inject constructor(): GeminiAiInterface {

    override suspend fun generateTextUsingImage(
        bitmapImage: Bitmap,
        text: String,
        generativeModel: GenerativeModel
    ): GenerateContentResponse {
        val prompt = content {
            image(bitmapImage)
            text(text)
        }
        val response = generativeModel.generateContent(prompt)
        return response
    }

    override suspend fun generateText(
        text: String,
        generativeModel: GenerativeModel
    ): GenerateContentResponse {
        val response = generativeModel.generateContent(text)
        return response
    }
}

interface GeminiAiInterface {

    suspend fun generateTextUsingImage(
        bitmapImage: Bitmap,
        text: String,
        generativeModel: GenerativeModel = Firebase.vertexAI.generativeModel("gemini-1.5-flash")
    ): GenerateContentResponse

    suspend fun generateText(
        text: String,
        generativeModel: GenerativeModel = Firebase.vertexAI.generativeModel("gemini-1.5-flash")
    ): GenerateContentResponse
}