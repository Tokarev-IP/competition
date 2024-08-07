package com.example.catalog.content.domain.usecases.network

import android.net.Uri
import com.example.catalog.content.data.repositories.FirebaseStorageDownloadRepository
import com.example.catalog.content.domain.interfaces.DownloadFileUseCaseInterface
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class DownloadFileUseCase @Inject constructor(
    private val firebaseStorageDownloadRepository: FirebaseStorageDownloadRepository,
) : DownloadFileUseCaseInterface {

    override suspend fun downloadUriOfMenuPicture(
        pathString: String,
        menuId: String,
    ): Uri {
        return suspendCancellableCoroutine { continuation ->
            firebaseStorageDownloadRepository.downloadUriOfFile(
                pathString = "$menuId/$pathString/main_picture",
                onSuccess = { uri: Uri? ->
                    if (uri != null)
                        continuation.resume(uri)
                    else
                        continuation.resumeWithException(NullPointerException("Uri is null"))
                },
                onFailure = { e: Exception ->
                    continuation.resumeWithException(e)
                }
            )
        }
    }

    override suspend fun downloadUriOfDishPicture(
        pathString: String,
        menuId: String,
        dishId: String,
    ): Uri {
        return suspendCancellableCoroutine { continuation ->
            firebaseStorageDownloadRepository.downloadUriOfFile(
                pathString = "$menuId/$pathString/$dishId",
                onSuccess = { uri: Uri? ->
                    if (uri != null)
                        continuation.resume(uri)
                    else
                        continuation.resumeWithException(NullPointerException("Uri is null"))
                },
                onFailure = { e: Exception ->
                    continuation.resumeWithException(e)
                }
            )
        }
    }

    override suspend fun downloadDishImageFile(pathString: String, menuId: String, dishId: String): ByteArray {
        return suspendCancellableCoroutine { continuation ->
            firebaseStorageDownloadRepository.downloadImageFile(
                pathString = "$menuId/$pathString/$dishId",
                onSuccess = { biteArray: ByteArray ->
                    continuation.resume(biteArray)
                },
                onFailure = { e: Exception ->
                    continuation.resumeWithException(e)
                }
            )

        }
    }
}