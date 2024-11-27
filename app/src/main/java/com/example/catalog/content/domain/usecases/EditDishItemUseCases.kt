package com.example.catalog.content.domain.usecases

import android.graphics.Bitmap
import android.net.Uri
import com.example.catalog.content.domain.data.DishData
import com.example.catalog.content.domain.usecases.logic.TransformBitmapImageInterface
import com.example.catalog.content.domain.usecases.logic.TransformImageUseCaseInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class EditDishItemUseCases @Inject constructor(
    private val transformBitmapImageInterface: TransformBitmapImageInterface,
    private val generateAiTextUseCaseInterface: GenerateAiTextUseCaseInterface,
    private val transformImageUseCaseInterface: TransformImageUseCaseInterface,
) : EditDishItemUseCasesInterface {

    override suspend fun transformUpdatedDishImage(
        imageBitmap: Bitmap,
        dishData: DishData,
        onUpdatedDish: (DishData) -> Unit,
        onErrorMessage: (String) -> Unit
    ) {
        try {
            val bitmap = withContext(Dispatchers.Default) {
                transformBitmapImageInterface.segmentImageFromBitmap(imageBitmap)
            }

            val croppedBitmap = withContext(Dispatchers.Default) {
                transformBitmapImageInterface.cropBitmapToForeground(bitmap)
            }
            onUpdatedDish(dishData.copy(updatedImageModel = croppedBitmap))
        } catch (e: Exception) {
            onErrorMessage(e.message.toString())
        }
    }

    override suspend fun generateDishDescription(
        dishImage: Bitmap,
        dishData: DishData,
        onUpdatedDish: (DishData) -> Unit,
        onErrorMessage: (String) -> Unit
    ) {
        try {
            val response = withContext(Dispatchers.IO) {
                generateAiTextUseCaseInterface.generateAiDescriptionOfDish(
                    imageBitmap = dishImage,
                    dishName = dishData.name,
                )
            }
            onUpdatedDish(dishData.copy(description = response))
        } catch (e: Exception) {
            onErrorMessage(e.message.toString())
        }
    }

    override suspend fun setUpdatedDishImage(
        imageUri: Uri,
        dishData: DishData,
        onUpdatedDish: (DishData) -> Unit,
        onErrorMessage: (String) -> Unit,
        quality: Int,
    ) {
        try {
            val bitmap = withContext(Dispatchers.Default) {
                transformImageUseCaseInterface.getBitmapFromUri(imageUri)
            }
            val compressedBitmap = withContext(Dispatchers.Default) {
                transformImageUseCaseInterface.compressBitmap(
                    bitmap = bitmap,
                    quality = quality,
                )
            }
            val updatedDishData = dishData.copy(updatedImageModel = compressedBitmap)
            onUpdatedDish(updatedDishData)
        } catch (e: Exception) {
            onErrorMessage(e.message.toString())
        }
    }
}

interface EditDishItemUseCasesInterface {
    suspend fun transformUpdatedDishImage(
        imageBitmap: Bitmap,
        dishData: DishData,
        onUpdatedDish: (DishData) -> Unit,
        onErrorMessage: (String) -> Unit
    )

    suspend fun generateDishDescription(
        dishImage: Bitmap,
        dishData: DishData,
        onUpdatedDish: (DishData) -> Unit,
        onErrorMessage: (String) -> Unit
    )

    suspend fun setUpdatedDishImage(
        imageUri: Uri,
        dishData: DishData,
        onUpdatedDish: (DishData) -> Unit,
        onErrorMessage: (String) -> Unit,
        quality: Int = 80,
    )
}