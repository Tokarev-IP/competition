package com.example.catalog.content.domain.usecases.logic

import android.content.Context
import android.net.Uri
import android.util.Log
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.label.ImageLabeling
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resumeWithException

class LabelImageUseCase {

    suspend fun labelImage(context: Context, uri: Uri): Int {
        return suspendCancellableCoroutine { continuation ->
            val image = InputImage.fromFilePath(context, uri)
            val labeler = ImageLabeling.getClient(ImageLabelerOptions.DEFAULT_OPTIONS)

            labeler.process(image)
                .addOnSuccessListener { labels ->
                    for (label in labels) {
                        val text = label.text
                        val confidence = label.confidence
                        val index = label.index
                        Log.d("DAVAI", "labelImage: $text")
                    }
                }
                .addOnFailureListener { e ->
                    continuation.resumeWithException(e)
                }
        }

    }
}