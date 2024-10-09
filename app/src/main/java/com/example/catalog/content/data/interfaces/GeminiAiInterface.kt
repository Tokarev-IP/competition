package com.example.catalog.content.data.interfaces

import android.graphics.Bitmap
import com.google.firebase.Firebase
import com.google.firebase.vertexai.GenerativeModel
import com.google.firebase.vertexai.type.GenerateContentResponse
import com.google.firebase.vertexai.vertexAI

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