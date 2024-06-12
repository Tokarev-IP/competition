package com.example.catalog.content.domain.usecases

import android.graphics.Bitmap
import android.util.Log
import com.example.catalog.content.data.interfaces.GeminiAiInterface
import com.example.catalog.content.domain.interfaces.GenerateAiTextUseCaseInterface
import javax.inject.Inject

class GenerateAiTextUseCase @Inject constructor(
    private val geminiAiInterface: GeminiAiInterface,
) : GenerateAiTextUseCaseInterface {

    override suspend fun generateFunnyNameOfDishUsingImage(
        imageBitmap: Bitmap,
        parameters: List<String?>,
        onResponse: (String) -> Unit,
        onFailure: () -> Unit
    ) {
        val text = StringBuffer("Generate a name for the dish using this image and parameters: ")
        parameters.forEach { parameter ->
            if (parameter != null)
                text.append("$parameter; ")
        }
//        val text = "Generate funny name of dish using this image"
        Log.d("DAVAI", text.toString())
        val response =
            geminiAiInterface.generateTextUsingImage(text = text.toString(), bitmapImage = imageBitmap)
        response.text?.let { responseText ->
            Log.d("DAVAI", "response text - $responseText")
            onResponse(responseText)
        }
        if (response.text == null)
            onFailure()
    }
}