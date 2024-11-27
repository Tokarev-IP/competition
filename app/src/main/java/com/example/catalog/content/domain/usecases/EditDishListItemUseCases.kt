package com.example.catalog.content.domain.usecases

import com.example.catalog.content.data.adapters.FirebaseStorageDeleteAdapterInterface
import com.example.catalog.content.data.adapters.FirebaseStorageDownloadAdapterInterface
import com.example.catalog.content.data.adapters.FirebaseStorageUploadAdapterInterface
import com.example.catalog.content.data.adapters.FirestoreDeleteAdapterInterface
import com.example.catalog.content.data.adapters.FirestoreUploadAdapterInterface
import com.example.catalog.content.domain.data.DishData
import com.example.catalog.content.domain.extensions.addDishItem
import com.example.catalog.content.domain.extensions.removeDishItem
import com.example.catalog.content.domain.extensions.toDishDataFirebase
import com.example.catalog.content.domain.usecases.logic.TransformImageUseCaseInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class EditDishListItemUseCases @Inject constructor(
    private val transformImageUseCaseInterface: TransformImageUseCaseInterface,
    private val firebaseStorageUploadAdapterInterface: FirebaseStorageUploadAdapterInterface,
    private val firebaseStorageDownloadAdapterInterface: FirebaseStorageDownloadAdapterInterface,
    private val firebaseStorageDeleteAdapterInterface: FirebaseStorageDeleteAdapterInterface,
    private val firestoreUploadAdapterInterface: FirestoreUploadAdapterInterface,
    private val firestoreDeleteAdapterInterface: FirestoreDeleteAdapterInterface,
) : EditDishListItemUseCasesInterface {

    override suspend fun saveDishItem(
        menuId: String,
        dishData: DishData,
        dishList: List<DishData>,
        onDishListUpdated: (List<DishData>) -> Unit,
        onErrorMessage: (String) -> Unit,
    ) {
        try {
            dishData.updatedImageModel?.let { bitmap ->
                val byteArrayOfBitmapImage =
                    withContext(Dispatchers.Default) { //transforming image to byte array
                        transformImageUseCaseInterface.getByteArrayFromBitmap(bitmap = bitmap)
                    }
                withContext(Dispatchers.IO) { //uploading image to firebase
                    firebaseStorageUploadAdapterInterface.uploadDishImageUsingByteArray(
                        menuId = menuId,
                        dishId = dishData.id,
                        byteArray = byteArrayOfBitmapImage,
                    )
                }
                val uri = withContext(Dispatchers.IO) { //receiving uri of uploaded image
                    firebaseStorageDownloadAdapterInterface.downloadUriOfDishImage(
                        menuId = menuId,
                        dishId = dishData.id,
                    )
                }
                val newDishData = dishData.copy(imageModel = uri)
                withContext(Dispatchers.IO) { //uploading the data of the dish to firestore
                    firestoreUploadAdapterInterface.uploadMenuDishData(
                        data = newDishData.toDishDataFirebase(),
                        menuId = menuId,
                        dishId = dishData.id,
                    )
                }
                val newDishList =
                    withContext(Dispatchers.IO) { //add the data of the dish to dish list
                        dishList.addDishItem(newDishData)
                    }
                onDishListUpdated(newDishList)
            } ?: run {
                withContext(Dispatchers.IO) { //uploading the data of the dish to firestore
                    firestoreUploadAdapterInterface.uploadMenuDishData(
                        data = dishData.toDishDataFirebase(),
                        menuId = menuId,
                        dishId = dishData.id,
                    )
                }
                val newDishList =
                    withContext(Dispatchers.IO) { //get new dish list with updated dish
                        dishList.addDishItem(dishData)
                    }
                onDishListUpdated(newDishList)
            }
        } catch (e: Exception) {
            onErrorMessage(e.message.toString())
        }
    }

    override suspend fun deleteDishItem(
        menuId: String,
        dishId: String,
        dishList: List<DishData>,
        onDishListUpdated: (List<DishData>) -> Unit,
        onErrorMessage: (String) -> Unit
    ) {
        try {
            withContext(Dispatchers.IO) { //delete the data of the dish from firestore
                firestoreDeleteAdapterInterface.deleteMenuDishData(
                    menuId = menuId,
                    dishId = dishId,
                )
            }
            val isImageDishExisted =
                withContext(Dispatchers.IO) { //check if the image of the dish exists
                    firebaseStorageDownloadAdapterInterface.checkIfDishImageExists(
                        menuId = menuId,
                        dishId = dishId,
                    )
                }
            if (isImageDishExisted)
                withContext(Dispatchers.IO) { //delete the image of the dish from firebase
                    firebaseStorageDeleteAdapterInterface.deleteDishImage(
                        menuId = menuId,
                        dishId = dishId,
                    )
                }
            val newDishList =
                withContext(Dispatchers.IO) { //delete the data of the dish from dish list
                    dishList.removeDishItem(dishId)
                }
            onDishListUpdated(newDishList)
        } catch (e: Exception) {
            onErrorMessage(e.message.toString())
        }
    }
}

interface EditDishListItemUseCasesInterface {
    suspend fun saveDishItem(
        menuId: String,
        dishData: DishData,
        dishList: List<DishData>,
        onDishListUpdated: (List<DishData>) -> Unit = {},
        onErrorMessage: (String) -> Unit = {},
    )

    suspend fun deleteDishItem(
        menuId: String,
        dishId: String,
        dishList: List<DishData>,
        onDishListUpdated: (List<DishData>) -> Unit = {},
        onErrorMessage: (String) -> Unit = {},
    )
}