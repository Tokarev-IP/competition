package com.example.catalog.content.domain.usecases.logic

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import com.example.catalog.content.domain.functions.TransformImageFunctionsInterface
import com.example.catalog.content.domain.interfaces.TransformImageUseCaseInterface
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.suspendCancellableCoroutine
import java.io.ByteArrayOutputStream
import javax.inject.Inject
import kotlin.coroutines.resume

class TransformImageUseCase @Inject constructor(
    @ApplicationContext private val appContext: Context,
) : TransformImageFunctionsInterface, TransformImageUseCaseInterface {

    override suspend fun getBitmapFromUri(imageUri: Uri): Bitmap {
        return suspendCancellableCoroutine { continuation ->
            val bitmap = transformImageFromUriToBitmap(imageUri = imageUri, appContext = appContext)
            continuation.resume(bitmap)
        }
    }

    override suspend fun getByteArrayFromBitmap(
        bitmap: Bitmap,
        format: Bitmap.CompressFormat,
        quality: Int,
    ): ByteArray {
        return suspendCancellableCoroutine { continuation ->
            val byteArrayOutputStream = ByteArrayOutputStream()
            bitmap.compress(format, quality, byteArrayOutputStream)
            val byteArray = byteArrayOutputStream.toByteArray()
            continuation.resume(byteArray)
        }
    }

    override suspend fun compressBitmap(
        bitmap: Bitmap,
        format: Bitmap.CompressFormat,
        quality: Int
    ): Bitmap {
         return suspendCancellableCoroutine { continuation ->
             val outputStream = ByteArrayOutputStream()

             bitmap.compress(format, quality, outputStream)

             val byteArray = outputStream.toByteArray()

             val compressedBitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)

             continuation.resume(compressedBitmap)
         }
    }

}