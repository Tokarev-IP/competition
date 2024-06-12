package com.example.catalog.content.domain.interfaces

import android.graphics.Bitmap

interface GenerateAiTextUseCaseInterface {

    suspend fun generateFunnyNameOfDishUsingImage(
        imageBitmap: Bitmap,
        parameters: List<String?>,
        onResponse: (String) -> Unit,
        onFailure: () -> Unit
    )
}