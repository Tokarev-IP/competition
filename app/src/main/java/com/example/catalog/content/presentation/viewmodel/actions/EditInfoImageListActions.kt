package com.example.catalog.content.presentation.viewmodel.actions

import android.net.Uri
import com.example.catalog.content.domain.data.InfoImageData
import com.example.catalog.content.domain.data.InfoImageFirebase
import com.example.catalog.content.domain.usecases.logic.TransformImageUseCaseInterface
import com.example.catalog.content.domain.usecases.network.DeleteDataUseCaseInterface
import com.example.catalog.content.domain.usecases.network.DeleteFileUseCaseInterface
import com.example.catalog.content.domain.usecases.network.DownloadFileUseCaseInterface
import com.example.catalog.content.domain.usecases.network.UploadDataUseCaseInterface
import com.example.catalog.content.domain.usecases.network.UploadFileUseCaseInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class EditInfoImageListActions @Inject constructor(
    private val uploadDataUseCaseInterface: UploadDataUseCaseInterface,
    private val uploadFileUseCaseInterface: UploadFileUseCaseInterface,
    private val transformImageUseCaseInterface: TransformImageUseCaseInterface,
    private val downloadFileUseCaseInterface: DownloadFileUseCaseInterface,
    private val deleteDataUseCaseInterface: DeleteDataUseCaseInterface,
    private val deleteFileUseCaseInterface: DeleteFileUseCaseInterface,
) : EditInfoImageListActionsInterface {

    override suspend fun saveInfoImageItem(
        menuId: String,
        imageId: String,
        uri: Uri,
        infoImageList: List<InfoImageData>,
        onInfoImageListUpdated: (List<InfoImageData>) -> Unit,
        onErrorMessage: (String) -> Unit,
        compressQuality: Int,
    ) {
        try {
            withContext(Dispatchers.IO) { //uploading image to firebase
                uploadFileUseCaseInterface.uploadInfoImageUsingUri(
                    menuId = menuId,
                    imageId = imageId,
                    uri = uri,
                )
            }
            val uriOfImage = withContext(Dispatchers.IO) { //receiving uri of uploaded byte array
                downloadFileUseCaseInterface.downloadUriOfInfoImage(
                    menuId = menuId,
                    imageId = imageId,
                )
            }
            withContext(Dispatchers.IO) { //uploading data to firestore
                uploadDataUseCaseInterface.uploadInfoImageData(
                    data = InfoImageFirebase(
                        id = imageId,
                        image = uriOfImage.toString()
                    ),
                    menuId = menuId,
                    imageId = imageId,
                )
            }
            val infoImageMutableList = infoImageList.toMutableList()
            infoImageMutableList.add(
                InfoImageData(
                    id = imageId,
                    imageModel = uriOfImage
                )
            )
            onInfoImageListUpdated(infoImageMutableList.toList())
        } catch (e: Exception) {
            onErrorMessage(e.message.toString())
        }
    }

    override suspend fun deleteInfoImageItem(
        menuId: String,
        imageId: String,
        infoImageList: List<InfoImageData>,
        onInfoImageListUpdated: (List<InfoImageData>) -> Unit,
        onErrorMessage: (String) -> Unit
    ) {
        try {
            withContext(Dispatchers.IO) { //delete image file from firebase
                deleteFileUseCaseInterface.deleteInfoImage(
                    menuId = menuId,
                    imageId = imageId,
                )
            }
            withContext(Dispatchers.IO) { //delete image data from firestore
                deleteDataUseCaseInterface.deleteInfoImageData(
                    menuId = menuId,
                    imageId = imageId,
                )
            }
            val infoImageMutableList = infoImageList.toMutableList()
            infoImageMutableList.removeIf { it.id == imageId }
            onInfoImageListUpdated(infoImageMutableList.toList())
        } catch (e: Exception) {
            onErrorMessage(e.message.toString())
        }
    }
}

interface EditInfoImageListActionsInterface {
    suspend fun saveInfoImageItem(
        menuId: String,
        imageId: String,
        uri: Uri,
        infoImageList: List<InfoImageData>,
        onInfoImageListUpdated: (List<InfoImageData>) -> Unit,
        onErrorMessage: (String) -> Unit,
        compressQuality: Int = 50,
    )

    suspend fun deleteInfoImageItem(
        menuId: String,
        imageId: String,
        infoImageList: List<InfoImageData>,
        onInfoImageListUpdated: (List<InfoImageData>) -> Unit,
        onErrorMessage: (String) -> Unit,
    )
}