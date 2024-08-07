package com.example.catalog.content.domain.functions

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import java.io.ByteArrayOutputStream
import java.io.InputStream


interface TransformImageFunctionsInterface {

    fun compressAndTransformImageFromUriToBitmap(
        imageUri: Uri,
        maxWidth: Int,
        maxHeight: Int,
        quality: Int,
        appContext: Context,
    ): Bitmap {
        val imageStream: InputStream? = appContext.contentResolver.openInputStream(imageUri)

        val bitmap = BitmapFactory.decodeStream(imageStream)

        var width = bitmap.width
        var height = bitmap.height
        val aspectRatio: Float = width.toFloat() / height.toFloat()

        if (width > maxWidth || height > maxHeight) {
            if (aspectRatio > 1) {
                width = maxWidth
                height = (width / aspectRatio).toInt()
            } else {
                height = maxHeight
                width = (height * aspectRatio).toInt()
            }
        }

        return Bitmap.createScaledBitmap(bitmap, width, height, true)
    }

    fun compressAndTransformImageFromUriToByteArray(
        imageUri: Uri,
        maxWidth: Int,
        maxHeight: Int,
        quality: Int,
        appContext: Context,
    ): ByteArray {
        val imageStream: InputStream? = appContext.contentResolver.openInputStream(imageUri)

        val bitmap = BitmapFactory.decodeStream(imageStream)

        var width = bitmap.width
        var height = bitmap.height
        val aspectRatio: Float = width.toFloat() / height.toFloat()

        if (width > maxWidth || height > maxHeight) {
            if (aspectRatio > 1) {
                width = maxWidth
                height = (width / aspectRatio).toInt()
            } else {
                height = maxHeight
                width = (height * aspectRatio).toInt()
            }
        }

        val scaledBitmap = Bitmap.createScaledBitmap(bitmap, width, height, true)
        val outputStream = ByteArrayOutputStream()
        scaledBitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)

        return outputStream.toByteArray()
    }

    fun transformImageFromUriToBitmap(
        imageUri: Uri,
        appContext: Context,
    ): Bitmap {
        val imageStream: InputStream? = appContext.contentResolver.openInputStream(imageUri)

        val bitmap = BitmapFactory.decodeStream(imageStream)

        return bitmap
    }

}