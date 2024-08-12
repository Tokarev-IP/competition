package com.example.catalog.content.domain.usecases.network

import com.example.catalog.content.data.interfaces.FirebaseStorageDeleteInterface
import com.example.catalog.content.domain.interfaces.DeleteFileUseCaseInterface
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class DeleteFileUseCase @Inject constructor(
    private val firebaseStorageDeleteInterface: FirebaseStorageDeleteInterface,
) : DeleteFileUseCaseInterface {

    override suspend fun deleteDish(
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
}