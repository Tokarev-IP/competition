package com.example.catalog.content.presentation.viewmodel.actions

import com.example.catalog.content.domain.data.MenuInfoData
import com.example.catalog.content.domain.extensions.toMenuInfoFirebase
import com.example.catalog.content.domain.usecases.network.DownloadFileUseCaseInterface
import com.example.catalog.content.domain.usecases.network.UploadDataUseCaseInterface
import com.example.catalog.content.domain.usecases.network.UploadFileUseCaseInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class EditMenuInfoActions @Inject constructor(
    private val uploadDataUseCaseInterface: UploadDataUseCaseInterface,
    private val uploadFileUseCaseInterface: UploadFileUseCaseInterface,
    private val downloadFileUseCaseInterface: DownloadFileUseCaseInterface,
) : EditMenuInfoActionsInterface {

    override suspend fun saveMenuInfoData(
        menuId: String,
        menuInfoData: MenuInfoData,
        onSuccess: () -> Unit,
        onErrorMessage: (String) -> Unit
    ) {
        try {
            menuInfoData.updatedImageModel?.let { imageUri ->
                withContext(Dispatchers.IO) { //uploading image to firebase
                    uploadFileUseCaseInterface.uploadMenuInfoImageUsingUri(
                        menuId = menuId,
                        uri = imageUri,
                    )
                }
                val uriFromFirebase =
                    withContext(Dispatchers.IO) { //receiving uri of uploaded image
                        downloadFileUseCaseInterface.downloadUriOfMenuInfoImage(
                            menuId = menuId,
                        )
                    }
                val newMenuInfoData = menuInfoData.copy(imageModel = uriFromFirebase)
                withContext(Dispatchers.IO) { //uploading data to firestore
                    uploadDataUseCaseInterface.uploadMenuInfoData(
                        data = newMenuInfoData.toMenuInfoFirebase(),
                        menuId = menuId
                    )
                }
            } ?: run {
                withContext(Dispatchers.IO) { //uploading data to firestore
                    uploadDataUseCaseInterface.uploadMenuInfoData(
                        data = menuInfoData.toMenuInfoFirebase(),
                        menuId = menuId
                    )
                }
            }
            onSuccess()
        } catch (e: Exception) {
            onErrorMessage(e.message.toString())
        }
    }
}

interface EditMenuInfoActionsInterface {
    suspend fun saveMenuInfoData(
        menuId: String,
        menuInfoData: MenuInfoData,
        onSuccess: () -> Unit,
        onErrorMessage: (String) -> Unit,
    )
}