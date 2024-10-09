package com.example.catalog.content.presentation.viewmodel.actions

import android.graphics.Bitmap
import android.net.Uri
import com.example.catalog.content.domain.data.DishData
import com.example.catalog.content.domain.usecases.logic.TransformBitmapImageUseCaseInterface
import com.example.catalog.content.domain.usecases.logic.TransformImageUseCaseInterface
import com.example.catalog.content.domain.usecases.network.GenerateAiTextUseCaseInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class EditDishItemActions @Inject constructor(
    private val transformBitmapImageUseCaseInterface: TransformBitmapImageUseCaseInterface,
    private val generateAiTextUseCaseInterface: GenerateAiTextUseCaseInterface,
    private val transformImageUseCaseInterface: TransformImageUseCaseInterface,
) : EditDishItemActionsInterface {

    override suspend fun transformUpdatedDishImage(
        imageBitmap: Bitmap,
        dishData: DishData,
        onUpdatedDish: (DishData) -> Unit,
        onErrorMessage: (String) -> Unit
    ) {
        try {
            val bitmap = withContext(Dispatchers.Default) {
                transformBitmapImageUseCaseInterface.segmentImageFromBitmap(imageBitmap)
            }

            val croppedBitmap = withContext(Dispatchers.Default) {
                transformBitmapImageUseCaseInterface.cropBitmapToForeground(bitmap)
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

interface EditDishItemActionsInterface {
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