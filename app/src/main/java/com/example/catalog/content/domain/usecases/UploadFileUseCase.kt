package com.example.catalog.content.domain.usecases

import com.example.catalog.content.data.repositories.FirebaseStorageUploadRepository
import com.example.catalog.content.domain.interfaces.UploadFileUseCaseInterface
import javax.inject.Inject

class UploadFileUseCase @Inject constructor(
    private val firebaseStorageUploadRepository: FirebaseStorageUploadRepository
) : UploadFileUseCaseInterface {

    override fun uploadMenuPictureUsingByteArray(
        pathString: String,
        menuId: String,
        byteArray: ByteArray,
        onSuccess: () -> Unit,
        onFailure: (msg: String) -> Unit
    ) {
        firebaseStorageUploadRepository.uploadFileUsingByteArray(
            pathString = "$menuId/$pathString/main_picture",
            bytes = byteArray,
            onSuccess = {
                onSuccess()
            },
            onFailure = { e: Exception ->
                onFailure(e.message.toString())
            }
        )

    }

    override fun uploadDishPictureUsingByteArray(
        pathString: String,
        menuId: String,
        dishId: String,
        byteArray: ByteArray,
        onSuccess: () -> Unit,
        onFailure: (msg: String) -> Unit
    ) {
        firebaseStorageUploadRepository.uploadFileUsingByteArray(
            pathString = "$menuId/$pathString/$dishId",
            bytes = byteArray,
            onSuccess = {
                onSuccess()
            },
            onFailure = { e: Exception ->
                onFailure(e.message.toString())
            }
        )
    }
}