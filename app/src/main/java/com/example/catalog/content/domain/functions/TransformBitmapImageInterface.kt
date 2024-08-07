package com.example.catalog.content.domain.functions

import android.graphics.Bitmap
import android.net.Uri

interface TransformBitmapImageInterface {

    suspend fun segmentImageFromUri(uri: Uri): Bitmap

    suspend fun segmentImageFromBitmap(bitmap: Bitmap): Bitmap

    suspend fun saveBitmapToGallery(bitmap: Bitmap)

    suspend fun addBackgroundToBitmap(foreground: Bitmap, color: Int): Bitmap

    suspend fun cropBitmapToForeground(bitmap: Bitmap): Bitmap

}