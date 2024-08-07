package com.example.catalog.content.domain.interfaces

import android.graphics.Bitmap
import android.net.Uri

interface TransformImageUseCaseInterface {

    suspend fun getBitmapFromUri(
        imageUri: Uri,
    ): Bitmap

    suspend fun getByteArrayFromBitmapWithQuality(
        bitmap: Bitmap,
        format: Bitmap.CompressFormat = Bitmap.CompressFormat.PNG,
        quality: Int = 100
    ): ByteArray

    suspend fun compressBitmap(
        bitmap: Bitmap,
        format: Bitmap.CompressFormat = Bitmap.CompressFormat.PNG,
        quality: Int
    ): Bitmap
}