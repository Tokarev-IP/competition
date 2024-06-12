package com.example.catalog.content.domain.usecases

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import com.example.catalog.content.domain.functions.TransformImageFunctionsInterface
import com.example.catalog.content.domain.interfaces.TransformImageUseCaseInterface
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class TransformImageUseCase @Inject constructor(
    @ApplicationContext private val appContext: Context,
): TransformImageFunctionsInterface, TransformImageUseCaseInterface {

    override fun getBitmapFromUriHighQuality(
        imageUri: Uri,
        maxWidth: Int,
        maxHeight: Int,
        quality: Int
    ): Bitmap {
        return compressAndTransformImageFromUriToBitmap(
            imageUri = imageUri,
            maxWidth = maxWidth,
            maxHeight = maxHeight,
            quality = quality,
            appContext = appContext
        )
    }

    override fun getBitmapFromUriMediumQuality(
        imageUri: Uri,
        maxWidth: Int,
        maxHeight: Int,
        quality: Int
    ): Bitmap {
        return compressAndTransformImageFromUriToBitmap(
            imageUri = imageUri,
            maxWidth = maxWidth,
            maxHeight = maxHeight,
            quality = quality,
            appContext = appContext
        )
    }

    override fun getBitmapFromUriLowQuality(
        imageUri: Uri,
        maxWidth: Int,
        maxHeight: Int,
        quality: Int
    ): Bitmap {
        return compressAndTransformImageFromUriToBitmap(
            imageUri = imageUri,
            maxWidth = maxWidth,
            maxHeight = maxHeight,
            quality = quality,
            appContext = appContext
        )
    }

    override fun getByteArrayFromUriHighQuality(
        imageUri: Uri,
        maxWidth: Int,
        maxHeight: Int,
        quality: Int
    ): ByteArray {
        return compressAndTransformImageFromUriToByteArray(
            imageUri = imageUri,
            maxWidth = maxWidth,
            maxHeight = maxHeight,
            quality = quality,
            appContext = appContext
        )
    }

    override fun getByteArrayFromUriMediumQuality(
        imageUri: Uri,
        maxWidth: Int,
        maxHeight: Int,
        quality: Int
    ): ByteArray {
        return compressAndTransformImageFromUriToByteArray(
            imageUri = imageUri,
            maxWidth = maxWidth,
            maxHeight = maxHeight,
            quality = quality,
            appContext = appContext
        )
    }

    override fun getByteArrayFromUriLowQuality(
        imageUri: Uri,
        maxWidth: Int,
        maxHeight: Int,
        quality: Int
    ): ByteArray {
        return compressAndTransformImageFromUriToByteArray(
            imageUri = imageUri,
            maxWidth = maxWidth,
            maxHeight = maxHeight,
            quality = quality,
            appContext = appContext
        )
    }
}