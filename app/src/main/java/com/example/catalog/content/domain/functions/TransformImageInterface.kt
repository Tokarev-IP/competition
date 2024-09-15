package com.example.catalog.content.domain.functions

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import java.io.ByteArrayOutputStream
import java.io.InputStream

interface TransformImageInterface {

    fun transformImageFromUriToBitmap(
        imageUri: Uri,
        appContext: Context,
    ): Bitmap {
        val imageStream: InputStream? = appContext.contentResolver.openInputStream(imageUri)

        val bitmap = BitmapFactory.decodeStream(imageStream)

        return bitmap
    }

}