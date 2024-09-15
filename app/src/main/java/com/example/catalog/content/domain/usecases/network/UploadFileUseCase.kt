package com.example.catalog.content.domain.usecases.network

import android.net.Uri
import com.example.catalog.content.data.repositories.FirebaseStorageUploadRepository
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class UploadFileUseCase @Inject constructor(
    private val firebaseStorageUploadRepository: FirebaseStorageUploadRepository
) : UploadFileUseCaseInterface {

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

    override suspend fun uploadMenuInfoImageUsingByteArray(
        pathString: String,
        menuId: String,
        byteArray: ByteArray
    ) {
        TODO("Not yet implemented")
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

    override suspend fun uploadInfoImageUsingByteArray(
        pathString: String,
        menuId: String,
        imageId: String,
        byteArray: ByteArray
    ) {
        TODO("Not yet implemented")
    }
}

interface UploadFileUseCaseInterface {
    suspend fun uploadMenuInfoImageUsingUri(
        pathString: String = "picture",
        menuId: String,
        uri: Uri,
    )

    suspend fun uploadMenuInfoImageUsingByteArray(
        pathString: String = "picture",
        menuId: String,
        byteArray: ByteArray,
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

    suspend fun uploadInfoImageUsingByteArray(
        pathString: String = "info",
        menuId: String,
        imageId: String,
        byteArray: ByteArray,
    )
}