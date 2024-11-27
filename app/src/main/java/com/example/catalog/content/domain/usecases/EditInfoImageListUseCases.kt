package com.example.catalog.content.domain.usecases

import android.net.Uri
import com.example.catalog.content.data.adapters.FirebaseStorageDeleteAdapterInterface
import com.example.catalog.content.data.adapters.FirebaseStorageDownloadAdapterInterface
import com.example.catalog.content.data.adapters.FirebaseStorageUploadAdapterInterface
import com.example.catalog.content.data.adapters.FirestoreDeleteAdapterInterface
import com.example.catalog.content.data.adapters.FirestoreUploadAdapterInterface
import com.example.catalog.content.domain.data.InfoImageData
import com.example.catalog.content.domain.data.InfoImageFirebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class EditInfoImageListUseCases @Inject constructor(
    private val firestoreUploadAdapterInterface: FirestoreUploadAdapterInterface,
    private val firebaseStorageUploadAdapterInterface: FirebaseStorageUploadAdapterInterface,
    private val firebaseStorageDownloadAdapterInterface: FirebaseStorageDownloadAdapterInterface,
    private val firestoreDeleteAdapterInterface: FirestoreDeleteAdapterInterface,
    private val firebaseStorageDeleteAdapterInterface: FirebaseStorageDeleteAdapterInterface,
) : EditInfoImageListUseCasesInterface {

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
                firebaseStorageUploadAdapterInterface.uploadInfoImageUsingUri(
                    menuId = menuId,
                    imageId = imageId,
                    uri = uri,
                )
            }
            val uriOfImage = withContext(Dispatchers.IO) { //receiving uri of uploaded byte array
                firebaseStorageDownloadAdapterInterface.downloadUriOfInfoImage(
                    menuId = menuId,
                    imageId = imageId,
                )
            }
            withContext(Dispatchers.IO) { //uploading data to firestore
                firestoreUploadAdapterInterface.uploadInfoImageData(
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
                firebaseStorageDeleteAdapterInterface.deleteInfoImage(
                    menuId = menuId,
                    imageId = imageId,
                )
            }
            withContext(Dispatchers.IO) { //delete image data from firestore
                firestoreDeleteAdapterInterface.deleteInfoImageData(
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

interface EditInfoImageListUseCasesInterface {
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