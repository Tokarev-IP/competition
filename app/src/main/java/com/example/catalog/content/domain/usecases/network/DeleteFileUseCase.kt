package com.example.catalog.content.domain.usecases.network

import com.example.catalog.content.data.interfaces.FirebaseStorageDeleteInterface
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class DeleteFileUseCase @Inject constructor(
    private val firebaseStorageDeleteInterface: FirebaseStorageDeleteInterface,
) : DeleteFileUseCaseInterface {

    override suspend fun deleteDishImage(
        pathString: String,
        menuId: String,
        dishId: String,
    ) {
        return suspendCancellableCoroutine<Unit> { continuation ->
            firebaseStorageDeleteInterface.deleteFile(
                pathString = "$menuId/$pathString/$dishId",
                onSuccess = {
                    continuation.resume(Unit)
                },
                onFailure = { e: Exception ->
                    continuation.resumeWithException(e)
                }
            )
        }
    }

    override suspend fun deleteInfoImage(
        pathString: String,
        menuId: String,
        imageId: String
    ) {
        return suspendCancellableCoroutine { continuation ->
            firebaseStorageDeleteInterface.deleteFile(
                pathString = "$menuId/$pathString/$imageId",
                onSuccess = {
                    continuation.resume(Unit)
                },
                onFailure = { e: Exception ->
                    continuation.resumeWithException(e)
                }
            )
        }
    }

    override suspend fun deleteMainImage(
        pathString: String,
        menuId: String
    ) {
        return suspendCancellableCoroutine { continuation ->
            firebaseStorageDeleteInterface.deleteFile(
                pathString = "$menuId/$pathString/$menuId",
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

interface DeleteFileUseCaseInterface {
    suspend fun deleteDishImage(
        pathString: String = "dish",
        menuId: String,
        dishId: String,
    )

    suspend fun deleteInfoImage(
        pathString: String = "info",
        menuId: String,
        imageId: String,
    )

    suspend fun deleteMainImage(
        pathString: String = "picture",
        menuId: String,
    )
}