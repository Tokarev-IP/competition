package com.example.catalog.content.presentation.viewmodel.actions

import com.example.catalog.content.domain.data.DishData
import com.example.catalog.content.domain.data.toDishDataFirebase
import com.example.catalog.content.domain.usecases.logic.DishListFunctionsInterface
import com.example.catalog.content.domain.usecases.logic.TransformImageUseCaseInterface
import com.example.catalog.content.domain.usecases.network.DeleteDataUseCaseInterface
import com.example.catalog.content.domain.usecases.network.DeleteFileUseCaseInterface
import com.example.catalog.content.domain.usecases.network.DownloadFileUseCaseInterface
import com.example.catalog.content.domain.usecases.network.UploadDataUseCaseInterface
import com.example.catalog.content.domain.usecases.network.UploadFileUseCaseInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class EditDishListActions @Inject constructor(
    private val transformImageUseCaseInterface: TransformImageUseCaseInterface,
    private val uploadFileUseCaseInterface: UploadFileUseCaseInterface,
    private val downloadFileUseCaseInterface: DownloadFileUseCaseInterface,
    private val uploadDataUseCaseInterface: UploadDataUseCaseInterface,
    private val dishListFunctionsInterface: DishListFunctionsInterface,
    private val deleteDataUseCaseInterface: DeleteDataUseCaseInterface,
    private val deleteFileUseCaseInterface: DeleteFileUseCaseInterface,
) : EditDishListActionsInterface {

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
                    uploadFileUseCaseInterface.uploadDishPictureUsingByteArray(
                        menuId = menuId,
                        dishId = dishData.id,
                        byteArray = byteArrayOfBitmapImage,
                    )
                }

                val uri = withContext(Dispatchers.IO) { //receiving uri of uploaded image
                    downloadFileUseCaseInterface.downloadUriOfDishPicture(
                        menuId = menuId,
                        dishId = dishData.id,
                    )
                }

                withContext(Dispatchers.IO) { //uploading the data of the dish to firestore
                    uploadDataUseCaseInterface.uploadMenuDishData(
                        data = dishData.toDishDataFirebase(),
                        menuId = menuId,
                        documentId = dishData.id,
                    )
                }

                val newDishList =
                    withContext(Dispatchers.IO) { //add the data of the dish to dish list
                        dishListFunctionsInterface.saveDishItemInDishList(
                            dishData = dishData.copy(imageModel = uri),
                            dishList = dishList,
                        )
                    }

                onDishListUpdated(newDishList)
            } ?: run {

                withContext(Dispatchers.IO) {
                    uploadDataUseCaseInterface.uploadMenuDishData(
                        data = dishData.toDishDataFirebase(),
                        menuId = menuId,
                        documentId = dishData.id,
                    )
                }

                val newDishList = withContext(Dispatchers.IO) {
                    dishListFunctionsInterface.saveDishItemInDishList(
                        dishData = dishData,
                        dishList = dishList,
                    )
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
                deleteDataUseCaseInterface.deleteMenuDish(
                    menuId = menuId,
                    dishId = dishId,
                )
            }

            val isImageDishExisted = withContext(Dispatchers.IO) { //check if the image of the dish exists
                downloadFileUseCaseInterface.checkIfDishImageExists(
                    menuId = menuId,
                    dishId = dishId,
                )
            }

            if (isImageDishExisted)
                withContext(Dispatchers.IO) { //delete the image of the dish from firebase
                    deleteFileUseCaseInterface.deleteDish(
                        menuId = menuId,
                        dishId = dishId,
                    )
                }

            val newDishList = withContext(Dispatchers.IO) { //delete the data of the dish from dish list
                dishListFunctionsInterface.deleteDishItemFromDishList(
                    dishId = dishId,
                    dishList = dishList,
                )
            }
            onDishListUpdated(newDishList)
        } catch (e: Exception) {
            onErrorMessage(e.message.toString())
        }
    }
}

interface EditDishListActionsInterface {
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