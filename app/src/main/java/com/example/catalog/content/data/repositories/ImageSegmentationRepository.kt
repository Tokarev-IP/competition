package com.example.catalog.content.data.repositories

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
import com.google.mlkit.vision.segmentation.subject.SubjectSegmenterOptions
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.IOException
import java.io.OutputStream
import javax.inject.Inject

class ImageSegmentationRepository @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    fun segmentImage(
        uri: Uri,
        onResult: (List<Bitmap>) -> Unit,
        onError: (String) -> Unit,
    ) {

//        val image = InputImage.fromBitmap(bitmap, 0)

        val bitmapList = mutableListOf<Bitmap>()

        var image: InputImage? = null

        try {
            image = InputImage.fromFilePath(context, uri)
        } catch (e: IOException) {
            e.printStackTrace()
        }

        val options = SubjectSegmenterOptions.Builder()
            .enableForegroundBitmap()
            .enableForegroundConfidenceMask()
            // enable options
            .build()

        val segmenter = SubjectSegmentation.getClient(options)

        if (image != null) {
            segmenter.process(image)
                .addOnSuccessListener { result ->

//                    val bitmapMask = Bitmap.createBitmap(
//                        Color.GREEN, image.width, image.height, Bitmap.Config.ARGB_8888, true
//                    )

//                    bitmapList.add(bitmapMask)


                    val foregroundBitmap = result.foregroundBitmap

                    if (foregroundBitmap != null) {
                        bitmapList.add(foregroundBitmap)

                        val foregroundBitmap2 = addBackgroundToBitmap(foregroundBitmap)
                        bitmapList.add(foregroundBitmap2)

                        bitmapList.add(cropBitmapToForeground(foregroundBitmap))
                    }

                    //
                    val colors = IntArray(image.width * image.height)

                    val foregroundMask = result.foregroundConfidenceMask
                    for (i in 0 until image.width * image.height) {
                        foregroundMask?.let {
                            if (foregroundMask[i] > 0.5f) {
                                colors[i] = Color.argb(128, 255, 0, 255)
                            }
                        }
                    }

                    val bitmapMask = Bitmap.createBitmap(
                        colors, image.width, image.height, Bitmap.Config.ARGB_8888
                    )
                    bitmapList.add(bitmapMask)
                    //



                    onResult(bitmapList)


                }
                .addOnFailureListener { e ->
                    // Task failed with an exception
                    // ...
                    onError(e.toString())
                }
        }


    }

    fun saveBitmapToGallery(bitmap: Bitmap) {
        val filename = "segmentImage.jpeg"

        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, filename)
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
            put(
                MediaStore.Images.Media.RELATIVE_PATH,
                Environment.DIRECTORY_PICTURES
            )
        }

        val resolver = context.contentResolver
        val uriImage = resolver.insert(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            contentValues
        )

        uriImage?.let {
            val outputStream: OutputStream? = resolver.openOutputStream(it)
            outputStream?.use { stream ->
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            }
        }
    }


    fun addBackgroundToBitmap(foreground: Bitmap): Bitmap {
        // Создаем новый Bitmap с размерами форграунда и конфигурацией форграунда
        val result = Bitmap.createBitmap(foreground.width, foreground.height, foreground.config)

        // Создаем Canvas для рисования на новом Bitmap
        val canvas = Canvas(result)

        // Заполняем фон нужным цветом
        canvas.drawColor(Color.YELLOW)

        // Затем рисуем форграунд поверх цветного фона
        canvas.drawBitmap(foreground, 0f, 0f, null)

        return result
    }

    fun cropBitmapToForeground(bitmap: Bitmap): Bitmap {
        val width = bitmap.width
        val height = bitmap.height

        var minX = width
        var minY = height
        var maxX = 0
        var maxY = 0

        // Найдем границы непрозрачных пикселей
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

        // Проверяем, что нашли границы
        if (minX < maxX && minY < maxY) {
            // Обрезаем Bitmap по найденным границам
            return Bitmap.createBitmap(bitmap, minX, minY, maxX - minX + 1, maxY - minY + 1)
        }

        // Если не нашли непрозрачных пикселей, возвращаем исходный Bitmap
        return bitmap
    }

}