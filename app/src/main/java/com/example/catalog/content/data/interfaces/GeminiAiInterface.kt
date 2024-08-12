package com.example.catalog.content.data.interfaces

import android.graphics.Bitmap
import com.google.ai.client.generativeai.type.GenerateContentResponse

interface GeminiAiInterface {

    suspend fun generateTextUsingImage(
        bitmapImage: Bitmap,
        text: String,
        texxt: String
    ): GenerateContentResponse

    suspend fun generateText(text: String, texxt: String): GenerateContentResponse
}