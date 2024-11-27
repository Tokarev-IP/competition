package com.example.catalog.content.domain.usecases

import com.example.catalog.content.data.adapters.FirebaseStorageDownloadAdapterInterface
import com.example.catalog.content.data.adapters.FirebaseStorageUploadAdapterInterface
import com.example.catalog.content.data.adapters.FirestoreUploadAdapterInterface
import com.example.catalog.content.domain.data.MenuInfoData
import com.example.catalog.content.domain.extensions.toMenuInfoFirebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class EditMenuInfoUseCases @Inject constructor(
    private val firestoreUploadAdapterInterface: FirestoreUploadAdapterInterface,
    private val firebaseStorageUploadAdapterInterface: FirebaseStorageUploadAdapterInterface,
    private val firebaseStorageDownloadAdapterInterface: FirebaseStorageDownloadAdapterInterface,
) : EditMenuInfoUseCasesInterface {

    override suspend fun saveMenuInfoData(
        menuId: String,
        menuInfoData: MenuInfoData,
        onSuccess: () -> Unit,
        onErrorMessage: (String) -> Unit
    ) {
        try {
            menuInfoData.updatedImageModel?.let { imageUri ->
                withContext(Dispatchers.IO) { //uploading image to firebase
                    firebaseStorageUploadAdapterInterface.uploadMenuInfoImageUsingUri(
                        menuId = menuId,
                        uri = imageUri,
                    )
                }
                val uriFromFirebase =
                    withContext(Dispatchers.IO) { //receiving uri of uploaded image
                        firebaseStorageDownloadAdapterInterface.downloadUriOfMenuInfoImage(
                            menuId = menuId,
                        )
                    }
                val newMenuInfoData = menuInfoData.copy(imageModel = uriFromFirebase)
                withContext(Dispatchers.IO) { //uploading data to firestore
                    firestoreUploadAdapterInterface.uploadMenuInfoData(
                        data = newMenuInfoData.toMenuInfoFirebase(),
                        menuId = menuId
                    )
                }
            } ?: run {
                withContext(Dispatchers.IO) { //uploading data to firestore
                    firestoreUploadAdapterInterface.uploadMenuInfoData(
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

interface EditMenuInfoUseCasesInterface {
    suspend fun saveMenuInfoData(
        menuId: String,
        menuInfoData: MenuInfoData,
        onSuccess: () -> Unit,
        onErrorMessage: (String) -> Unit,
    )
}