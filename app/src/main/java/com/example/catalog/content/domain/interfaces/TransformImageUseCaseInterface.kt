package com.example.catalog.content.domain.interfaces

import android.graphics.Bitmap
import android.net.Uri

interface TransformImageUseCaseInterface {

    fun getBitmapFromUriHighQuality(
        imageUri: Uri,
        maxWidth: Int = 800,
        maxHeight: Int = 800,
        quality: Int = 80,
    ): Bitmap

    fun getBitmapFromUriMediumQuality(
        imageUri: Uri,
        maxWidth: Int = 800,
        maxHeight: Int = 800,
        quality: Int = 60,
    ): Bitmap

    fun getBitmapFromUriLowQuality(
        imageUri: Uri,
        maxWidth: Int = 800,
        maxHeight: Int = 800,
        quality: Int = 40,
    ): Bitmap

    fun getByteArrayFromUriHighQuality(
        imageUri: Uri,
        maxWidth: Int = 800,
        maxHeight: Int = 800,
        quality: Int = 80,
    ): ByteArray

    fun getByteArrayFromUriMediumQuality(
        imageUri: Uri,
        maxWidth: Int = 800,
        maxHeight: Int = 800,
        quality: Int = 60,
    ): ByteArray

    fun getByteArrayFromUriLowQuality(
        imageUri: Uri,
        maxWidth: Int = 800,
        maxHeight: Int = 800,
        quality: Int = 40,
    ): ByteArray
}