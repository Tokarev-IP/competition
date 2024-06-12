package com.example.catalog.content.domain.usecases

import android.net.Uri
import com.example.catalog.content.data.repositories.FirebaseStorageDownloadRepository
import com.example.catalog.content.domain.interfaces.DownloadFileUseCaseInterface
import javax.inject.Inject

class DownloadFileUseCase @Inject constructor(
    private val firebaseStorageDownloadRepository: FirebaseStorageDownloadRepository,
) : DownloadFileUseCaseInterface {

    override fun downloadUriOfMenuPicture(
        pathString: String,
        menuId: String,
        onSuccess: (uri: Uri) -> Unit,
        onFailure: (msg: String) -> Unit
    ) {
        firebaseStorageDownloadRepository.downloadUriOfFile(
            pathString = "$menuId/$pathString/main_picture",
            onSuccess = { uri: Uri? ->
                if (uri != null)
                    onSuccess(uri)
                else
                    onFailure("Uri is null")
            },
            onFailure = { e: Exception ->
                onFailure(e.message.toString())
            }
        )
    }

    override fun downloadUriOfDishPicture(
        pathString: String,
        menuId: String,
        dishId: String,
        onSuccess: (uri: Uri) -> Unit,
        onFailure: (msg: String) -> Unit
    ) {
        firebaseStorageDownloadRepository.downloadUriOfFile(
            pathString = "$menuId/$pathString/$dishId",
            onSuccess = { uri: Uri? ->
                if (uri != null)
                    onSuccess(uri)
                else
                    onFailure("Uri is null")
            },
            onFailure = { e: Exception ->
                onFailure(e.message.toString())
            }
        )
    }
}