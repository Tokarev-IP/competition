package com.example.catalog.content.data.adapters

import android.net.Uri
import com.example.catalog.content.data.repositories.FirebaseStorageUploadRepository
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class FirebaseStorageUploadAdapter @Inject constructor(
    private val firebaseStorageUploadRepository: FirebaseStorageUploadRepository
) : FirebaseStorageUploadAdapterInterface {

    override suspend fun uploadMenuInfoImageUsingUri(
        pathString: String,
        menuId: String,
        uri: Uri
    ) {
        return suspendCancellableCoroutine { continuation ->
            firebaseStorageUploadRepository.uploadFileUsingUri(
                pathString = "$menuId/$pathString/$menuId",
                fileUri = uri,
                onSuccess = {
                    continuation.resume(Unit)
                },
                onFailure = { e: Exception ->
                    continuation.resumeWithException(e)
                }
            )
        }
    }

    override suspend fun uploadDishImageUsingByteArray(
        pathString: String,
        menuId: String,
        dishId: String,
        byteArray: ByteArray,
    ) {
        return suspendCancellableCoroutine { continuation ->
            firebaseStorageUploadRepository.uploadFileUsingByteArray(
                pathString = "$menuId/$pathString/$dishId",
                bytes = byteArray,
                onSuccess = {
                    continuation.resume(Unit)
                },
                onFailure = { e: Exception ->
                    continuation.resumeWithException(e)
                }
            )
        }
    }

    override suspend fun uploadInfoImageUsingUri(
        pathString: String,
        menuId: String,
        imageId: String,
        uri: Uri
    ) {
        return suspendCancellableCoroutine { continuation ->
            firebaseStorageUploadRepository.uploadFileUsingUri(
                pathString = "$menuId/$pathString/$imageId",
                fileUri = uri,
                onSuccess = {
                    continuation.resume(Unit)
                },
                onFailure = { e: Exception ->
                    continuation.resumeWithException(e)
                }
            )
        }
    }
}

interface FirebaseStorageUploadAdapterInterface {
    suspend fun uploadMenuInfoImageUsingUri(
        pathString: String = "picture",
        menuId: String,
        uri: Uri,
    )

    suspend fun uploadDishImageUsingByteArray(
        pathString: String = "dish",
        menuId: String,
        dishId: String,
        byteArray: ByteArray,
    )

    suspend fun uploadInfoImageUsingUri(
        pathString: String = "info",
        menuId: String,
        imageId: String,
        uri: Uri,
    )
}