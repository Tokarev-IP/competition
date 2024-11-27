package com.example.catalog.content.domain.usecases.logic

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.segmentation.subject.SubjectSegmentation
import com.google.mlkit.vision.segmentation.subject.SubjectSegmentationResult
import com.google.mlkit.vision.segmentation.subject.SubjectSegmenterOptions
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.suspendCancellableCoroutine
import java.io.OutputStream
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class TransformBitmapImage @Inject constructor(
    @ApplicationContext private val appContext: Context,
) : TransformBitmapImageInterface {

    private suspend fun segmentImage(inputImage: InputImage): Bitmap {
        return suspendCancellableCoroutine { continuation ->

            val options = SubjectSegmenterOptions.Builder()
                .enableForegroundBitmap()
                .enableForegroundConfidenceMask()
                .build()

            val segmenter = SubjectSegmentation.getClient(options)

            segmenter
                .process(inputImage)
                .addOnSuccessListener { result: SubjectSegmentationResult ->
                    val foregroundBitmap = result.foregroundBitmap
                    if (foregroundBitmap != null)
                        continuation.resume(foregroundBitmap)
                    else
                        continuation.resumeWithException(NullPointerException("Null foreground bitmap"))
                }
                .addOnFailureListener { exception ->
                    continuation.resumeWithException(exception)
                }

            continuation.invokeOnCancellation {
                segmenter.close()
            }
        }
    }

    override suspend fun segmentImageFromUri(
        uri: Uri,
    ): Bitmap {
        val image = InputImage.fromFilePath(appContext, uri)
        return segmentImage(inputImage = image)
    }

    override suspend fun segmentImageFromBitmap(
        bitmap: Bitmap,
    ): Bitmap {
        val image = InputImage.fromBitmap(bitmap, 0)
        return segmentImage(inputImage = image)
    }

    override suspend fun saveBitmapToGallery(
        bitmap: Bitmap,
    ) {
        return suspendCancellableCoroutine { continuation ->
            val filename = "DishImage.jpeg"

            val contentValues = ContentValues().apply {
                put(MediaStore.Images.Media.DISPLAY_NAME, filename)
                put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
                put(
                    MediaStore.Images.Media.RELATIVE_PATH,
                    Environment.DIRECTORY_PICTURES
                )
            }

            val resolver = appContext.contentResolver
            val uriImage = resolver.insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                contentValues
            )

            uriImage?.let {
                val outputStream: OutputStream? = resolver.openOutputStream(it)
                outputStream?.use { stream ->
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
                    continuation.resume(Unit)
                }
            }

            if (uriImage == null)
                continuation.resumeWithException(NullPointerException("Null uri of the image"))
        }
    }

    override suspend fun addBackgroundToBitmap(
        foreground: Bitmap,
        color: Int
    ): Bitmap {
        return suspendCoroutine { continuation ->
            val result = Bitmap.createBitmap(foreground.width, foreground.height, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(result)
            canvas.drawColor(color)
            canvas.drawBitmap(foreground, 0f, 0f, null)

            continuation.resume(result)
        }
    }

    override suspend fun cropBitmapToForeground(bitmap: Bitmap): Bitmap {
        return suspendCoroutine { continuation ->
            val width = bitmap.width
            val height = bitmap.height

            var minX = width
            var minY = height
            var maxX = 0
            var maxY = 0

            for (y in 0 until height) {
                for (x in 0 until width) {
                    if (bitmap.getPixel(x, y) != Color.TRANSPARENT) {
                        if (x < minX) minX = x
                        if (x > maxX) maxX = x
                        if (y < minY) minY = y
                        if (y > maxY) maxY = y
                    }
                }
            }

            if (minX < maxX && minY < maxY) {
                continuation.resume(
                    Bitmap.createBitmap(
                        bitmap,
                        minX,
                        minY,
                        maxX - minX,
                        maxY - minY,
                    )
                )
            }

            continuation.resume(bitmap)
        }
    }
}

interface TransformBitmapImageInterface {
    suspend fun segmentImageFromUri(uri: Uri): Bitmap

    suspend fun segmentImageFromBitmap(bitmap: Bitmap): Bitmap

    suspend fun saveBitmapToGallery(bitmap: Bitmap)

    suspend fun addBackgroundToBitmap(foreground: Bitmap, color: Int): Bitmap

    suspend fun cropBitmapToForeground(bitmap: Bitmap): Bitmap
}