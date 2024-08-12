package com.example.catalog.content.data.repositories

import android.graphics.Bitmap
import com.example.catalog.content.data.interfaces.GeminiAiInterface
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.GenerateContentResponse
import com.google.ai.client.generativeai.type.content
import javax.inject.Inject

class GeminiAiRepository @Inject constructor(): GeminiAiInterface {

    override suspend fun generateTextUsingImage(bitmapImage: Bitmap, text: String, texxt: String): GenerateContentResponse {
        val generativeModel = GenerativeModel(
            modelName = "gemini-1.5-pro-latest",
            apiKey = texxt
        )

        val inputContent = content {
            image(bitmapImage)
            text(text)
        }

        val response = generativeModel.generateContent(inputContent)
        return response
    }

    override suspend fun generateText(text: String, texxt: String): GenerateContentResponse {
        val generativeModel = GenerativeModel(
            modelName = "gemini-1.5-pro-latest",
            apiKey = texxt
        )

        val inputContent = content {
            text(text)
        }

        val response = generativeModel.generateContent(inputContent)
        return response
    }
}