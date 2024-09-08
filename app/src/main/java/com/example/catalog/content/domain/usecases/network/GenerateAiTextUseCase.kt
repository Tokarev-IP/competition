package com.example.catalog.content.domain.usecases.network

import android.graphics.Bitmap
import com.example.catalog.content.data.interfaces.GeminiAiInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GenerateAiTextUseCase @Inject constructor(
    private val geminiAiInterface: GeminiAiInterface,
) : GenerateAiTextUseCaseInterface {

    override suspend fun generateFunnyNameOfDishUsingImage(
        imageBitmap: Bitmap,
        parameters: List<String?>,
    ): String {
        return withContext(Dispatchers.IO) {
            val text =
                StringBuffer("Generate a name for the dish using this image and parameters: ")
            parameters.forEach { parameter ->
                if (parameter != null)
                    text.append("$parameter; ")
            }
            val response =
                geminiAiInterface.generateTextUsingImage(
                    text = text.toString(),
                    bitmapImage = imageBitmap,
                    texxt = string,
                )

            response.text ?: throw Exception("No response text received")
        }
    }

    override suspend fun translateText(
        text: String,
        language: String,
    ): String {
        return withContext(Dispatchers.IO) {
            val finalText =
                "Translate this text from to $language: $text (if it's imposable to translate, return original text)"

            val response = geminiAiInterface.generateText(
                text = finalText,
                texxt = string,
            )
            response.text ?: throw Exception("No response text received")
        }
    }

    override suspend fun generateAiDescriptionOfDish(
        imageBitmap: Bitmap,
        dishName: String,
    ): String {
        return withContext(Dispatchers.IO) {
            val finalText =
                "Having this image and name of the dish - ${dishName}; generate a description of the dish for a menu"

            val response =
                geminiAiInterface.generateTextUsingImage(
                    text = finalText,
                    bitmapImage = imageBitmap,
                    texxt = string,
                )

            response.text ?: throw Exception("No response text received")
        }
    }

    private val string = "AIzaSyAGbHdcGjdRGvxml2uMHmXhv6AEEAZOE-Y"
}

interface GenerateAiTextUseCaseInterface {
    suspend fun generateFunnyNameOfDishUsingImage(
        imageBitmap: Bitmap,
        parameters: List<String?>,
    ): String

    suspend fun translateText(
        text: String,
        language: String,
    ): String

    suspend fun generateAiDescriptionOfDish(
        imageBitmap: Bitmap,
        dishName: String,
    ): String
}