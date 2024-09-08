package com.example.catalog.content.domain.usecases.network

import com.example.catalog.content.data.repositories.FirebaseStorageUploadRepository
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class UploadFileUseCase @Inject constructor(
    private val firebaseStorageUploadRepository: FirebaseStorageUploadRepository
) : UploadFileUseCaseInterface {

    override suspend fun uploadMenuPictureUsingByteArray(
        pathString: String,
        menuId: String,
        byteArray: ByteArray,
    ) {
        return suspendCancellableCoroutine { continuation ->
            firebaseStorageUploadRepository.uploadFileUsingByteArray(
                pathString = "$menuId/$pathString/main_picture",
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

    override suspend fun uploadDishPictureUsingByteArray(
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
}

interface UploadFileUseCaseInterface {
    suspend fun uploadMenuPictureUsingByteArray(
        pathString: String = "pic",
        menuId: String,
        byteArray: ByteArray,
    )

    suspend fun uploadDishPictureUsingByteArray(
        pathString: String = "dish",
        menuId: String,
        dishId: String,
        byteArray: ByteArray,
    )
}