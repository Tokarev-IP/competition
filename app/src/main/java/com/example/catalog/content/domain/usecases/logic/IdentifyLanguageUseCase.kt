package com.example.catalog.content.domain.usecases.logic

import android.util.Log
import com.google.mlkit.nl.languageid.LanguageIdentification
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class IdentifyLanguageUseCase {

    suspend fun identifyLanguage(text: String): String? {
        return suspendCancellableCoroutine { continuation ->
            val languageIdentifier = LanguageIdentification.getClient()
            languageIdentifier.identifyLanguage(text)
                .addOnSuccessListener { languageCode ->
                    if (languageCode == "und") {
                        Log.d("DAVAI", "Can't identify language.")
                    } else {
                        Log.i("DAVAI", "Language: $languageCode")
                        continuation.resume(languageCode)
                    }
                }
                .addOnFailureListener { exception ->
                    continuation.resumeWithException(exception)
                }
        }
    }
}