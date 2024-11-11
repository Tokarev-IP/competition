package com.example.catalog.content.domain.usecases.network

import android.net.Uri
import com.example.catalog.content.data.repositories.FirebaseStorageDownloadRepository
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class DownloadFileUseCase @Inject constructor(
    private val firebaseStorageDownloadRepository: FirebaseStorageDownloadRepository,
) : DownloadFileUseCaseInterface {

    override suspend fun downloadUriOfDishImage(
        pathString: String,
        menuId: String,
        dishId: String,
    ): Uri {
        return suspendCancellableCoroutine { continuation ->
            firebaseStorageDownloadRepository.downloadUriOfFile(
                pathString = "$menuId/$pathString/$dishId",
                onSuccess = { uri: Uri? ->
                    uri?.let {
                        continuation.resume(it)
                    } ?: continuation.resumeWithException(NullPointerException("Uri is null"))
                },
                onFailure = { e: Exception ->
                    continuation.resumeWithException(e)
                }
            )
        }
    }

    override suspend fun downloadDishImageFile(
        pathString: String,
        menuId: String,
        dishId: String
    ): ByteArray {
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

    override suspend fun checkIfDishImageExists(
        pathString: String,
        menuId: String,
        dishId: String
    ): Boolean {
        return suspendCancellableCoroutine { continuation ->
            firebaseStorageDownloadRepository.getNameOfFile(
                pathString = "$menuId/$pathString/$dishId",
                onSuccess = { name: String? ->
                    name?.let {
                        continuation.resume(false)
                    } ?: continuation.resume(true)
                },
                onFailure = { e: Exception ->
                    continuation.resumeWithException(e)
                }
            )
        }
    }

    override suspend fun downloadUriOfInfoImage(
        pathString: String,
        menuId: String,
        imageId: String
    ): Uri {
        return suspendCancellableCoroutine { continuation ->
            firebaseStorageDownloadRepository.downloadUriOfFile(
                pathString = "$menuId/$pathString/$imageId",
                onSuccess = { uri: Uri? ->
                    uri?.let {
                        continuation.resume(it)
                    } ?: continuation.resumeWithException(NullPointerException("Uri is null"))
                },
                onFailure = { e: Exception ->
                    continuation.resumeWithException(e)
                }
            )
        }
    }

    override suspend fun downloadUriOfMenuInfoImage(pathString: String, menuId: String): Uri {
        return suspendCancellableCoroutine { continuation ->
            firebaseStorageDownloadRepository.downloadUriOfFile(
                pathString = "$menuId/$pathString/$menuId",
                onSuccess = { uri: Uri? ->
                    uri?.let {
                        continuation.resume(it)
                    } ?: continuation.resumeWithException(NullPointerException("Uri is null"))
                },
                onFailure = { e: Exception ->
                    continuation.resumeWithException(e)
                }
            )
        }
    }
}

interface DownloadFileUseCaseInterface {
    suspend fun downloadUriOfDishImage(
        pathString: String = "dish",
        menuId: String,
        dishId: String,
    ): Uri

    suspend fun downloadDishImageFile(
        pathString: String = "dish",
        menuId: String,
        dishId: String,
    ): ByteArray

    suspend fun checkIfDishImageExists(
        pathString: String = "dish",
        menuId: String,
        dishId: String,
    ): Boolean

    suspend fun downloadUriOfInfoImage(
        pathString: String = "info",
        menuId: String,
        imageId: String,
    ): Uri

    suspend fun downloadUriOfMenuInfoImage(
        pathString: String = "picture",
        menuId: String,
    ): Uri
}