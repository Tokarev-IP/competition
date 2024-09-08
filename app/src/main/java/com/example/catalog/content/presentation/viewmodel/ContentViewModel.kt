package com.example.catalog.content.presentation.viewmodel

import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.viewModelScope
import com.example.catalog.content.domain.data.DishData
import com.example.catalog.content.domain.data.SectionDataFirebase
import com.example.catalog.content.domain.usecases.logic.CreateDocFileInterface
import com.example.catalog.content.domain.usecases.logic.SaveMenuPdfFileUseCaseInterface
import com.example.catalog.content.domain.usecases.logic.TransformImageUseCaseInterface
import com.example.catalog.content.domain.usecases.network.DeleteDataUseCaseInterface
import com.example.catalog.content.domain.usecases.network.DeleteFileUseCaseInterface
import com.example.catalog.content.domain.usecases.network.DownloadDataUseCaseInterface
import com.example.catalog.content.domain.usecases.network.DownloadFileUseCaseInterface
import com.example.catalog.content.domain.usecases.network.GenerateAiTextUseCaseInterface
import com.example.catalog.content.domain.usecases.network.UploadDataUseCaseInterface
import com.example.catalog.content.domain.usecases.network.UploadFileUseCaseInterface
import com.example.catalog.content.presentation.ContentUiEvents
import com.example.catalog.content.presentation.ContentUiIntents
import com.example.catalog.content.presentation.ContentUiStates
import com.example.catalog.content.presentation.base.ContentBaseViewModel
import com.example.catalog.content.presentation.viewmodel.actions.EditDishItemActionsInterface
import com.example.catalog.content.presentation.viewmodel.actions.EditDishListActionsInterface
import com.example.catalog.content.presentation.viewmodel.actions.GetDataActionsInterface
import com.example.catalog.content.presentation.viewmodel.actions.MenuActionsInterface
import com.example.catalog.content.presentation.viewmodel.actions.PdfFileActionsInterface
import com.example.catalog.login.domain.interfaces.PhoneAuthUseCaseInterface
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class ContentViewModel @Inject constructor(
    private val downloadDataUseCaseInterface: DownloadDataUseCaseInterface,
    private val uploadDataUseCaseInterface: UploadDataUseCaseInterface,
    private val phoneAuthUseCaseInterface: PhoneAuthUseCaseInterface,
    private val uploadFileUseCaseInterface: UploadFileUseCaseInterface,
    private val downloadFileUseCaseInterface: DownloadFileUseCaseInterface,
    private val generateAiTextUseCaseInterface: GenerateAiTextUseCaseInterface,
    private val transformImageUseCaseInterface: TransformImageUseCaseInterface,
    private val createDicFileInterface: CreateDocFileInterface,
    private val deleteDataUseCaseInterface: DeleteDataUseCaseInterface,
    private val deleteFileUseCaseInterface: DeleteFileUseCaseInterface,
    private val saveMenuPdfFileUseCaseInterface: SaveMenuPdfFileUseCaseInterface,

    private val editDishListActionsInterface: EditDishListActionsInterface,
    private val editDishItemActionsInterface: EditDishItemActionsInterface,
    private val getDataActionsInterface: GetDataActionsInterface,
    private val pdfFileActionsInterface: PdfFileActionsInterface,
    private val menuActionsInterface: MenuActionsInterface,
) : ContentBaseViewModel<ContentUiStates, ContentUiIntents, ContentUiEvents>(ContentUiStates.Loading) {

    private var menuIdVM: String? = null
    private var userIdVM: String? = null

    override fun setUiEvent(uiEvent: ContentUiEvents) {
        when (uiEvent) {
            is ContentUiEvents.GoBack -> {
                setUiIntent(ContentUiIntents.GoBackNavigation)
            }

            is ContentUiEvents.EditDishItem -> {
                setDishItemData(data = uiEvent.dishData)
                setUiIntent(ContentUiIntents.GoToEditDishScreen)
            }

            is ContentUiEvents.DownloadMenuList -> {
                menuIdVM?.let { id ->
                    getDishDataList(menuId = id)
                } ?: setUiIntent(ContentUiIntents.GoToCheckIdScreen)
            }

            is ContentUiEvents.SaveDishItem -> {
                menuIdVM?.let { id ->
                    saveDishItem(
                        menuId = id,
                        dishData = uiEvent.dishData,
                        dishList = getDishList(),
                    )
                } ?: setUiIntent(ContentUiIntents.GoToCheckIdScreen)
            }

            is ContentUiEvents.CreateDishItem -> {
                setDishItemData(data = DishData(id = UUID.randomUUID().toString()))
                setUiIntent(ContentUiIntents.GoToEditDishScreen)
            }

            is ContentUiEvents.CreateMenu -> {
                userIdVM?.let { id ->
                    createMenuId(
                        userId = id,
                        menuId = UUID.randomUUID().toString()
                    )
                } ?: setUiIntent(ContentUiIntents.GoToCheckIdScreen)
            }

            is ContentUiEvents.CheckMenuId -> {
                userIdVM?.let { id ->
                    getCurrentMenuId(id)
                } ?: checkMenuId()
            }

            is ContentUiEvents.GenerateDescriptionOfDish -> {
                generateDishDescription(
                    dishImage = uiEvent.imageBitmap,
                    dishData = getDishItemData()
                )
            }

            is ContentUiEvents.CreateUpdatedImage -> {
                updateDishImage(
                    imageBitmap = uiEvent.imageBitmap,
                    dishData = getDishItemData()
                )
            }


            is ContentUiEvents.SetInitialImage -> {
                setInitialImage(
                    imageUri = uiEvent.imageUri,
                    dishData = getDishItemData()
                )
            }

            is ContentUiEvents.SaveMenuAsDocFile -> {
                menuIdVM?.let { id ->
                    uiEvent.language?.let { language ->
                        createTranslatedMenuDocFile(
                            menuId = id,
                            folderUri = uiEvent.folderUri,
                            dishList = getDishList(),
                            translateLanguage = language,
                        )
                    } ?: createMenuDocFile(
                        menuId = id,
                        folderUri = uiEvent.folderUri,
                        dishList = getDishList(),
                    )
                } ?: setUiIntent(ContentUiIntents.GoToCheckIdScreen)
            }

            is ContentUiEvents.SetNamePriceWeightDescription -> {
                setDishItemData(
                    getDishItemData().copy(
                        name = uiEvent.name,
                        price = uiEvent.price.toDouble(),
                        weight = uiEvent.weight.toDouble(),
                        description = uiEvent.description
                    )
                )
            }

            is ContentUiEvents.DeleteDish -> {
                menuIdVM?.let { id ->
                    deleteDish(
                        menuId = id,
                        dishId = uiEvent.dishData.id,
                        dishList = getDishList(),
                    )
                } ?: setUiIntent(ContentUiIntents.GoToCheckIdScreen)
            }

            is ContentUiEvents.SaveMenuAsPdfFile -> {
                setUiState(ContentUiStates.Loading)

                setUiState(ContentUiStates.Show)
            }
        }
    }

    private fun checkMenuId() {
        setUiState(ContentUiStates.Loading)
        viewModelScope.launch {
            phoneAuthUseCaseInterface.getCurrentUser(
                onUserExists = { firebaseUser: FirebaseUser ->
                    userIdVM = firebaseUser.uid
                    getCurrentMenuId(firebaseUser.uid)
                },
                onUserDoesNotExist = {
                    setUiState(ContentUiStates.Error)
                }
            )
        }
    }

    private fun getCurrentMenuId(userId: String) {
        setUiState(ContentUiStates.Loading)
        viewModelScope.launch {
            getDataActionsInterface.getCurrentMenuId(
                userId = userId,
                onEmptyMenuId = {
                    setUiIntent(ContentUiIntents.GoToCreateMenuScreen)
                    setUiState(ContentUiStates.Show)
                },
                onMenuId = { menuId ->
                    menuIdVM = menuId
                    setUiIntent(ContentUiIntents.GoToDishListScreen)
                    getDishDataList(menuId)
                },
                onErrorMessage = { errorMessage ->
                    setUiIntent(ContentUiIntents.ShowSnackBarMsg(errorMessage))
                    setUiState(ContentUiStates.Error)
                }
            )
        }
//        viewModelScope.launch {
//            try {
//                val data = withContext(Dispatchers.IO) {
//                    downloadDataUseCaseInterface.downloadMenuId(userId = userId)
//                }
//                if (data == null) {
//                    setUiState(ContentUiStates.Show)
//                } else {
//                    menuIdVM = data.id
//                    setUiIntent(ContentUiIntents.GoToDishListScreen)
//                }
//            } catch (e: Exception) {
//                setUiState(ContentUiStates.Error)
//                setUiIntent(ContentUiIntents.ShowSnackBarMsg(e.message.toString()))
//            }
//        }
    }

    private fun getDishDataList(menuId: String) {
        setUiState(ContentUiStates.Loading)
        viewModelScope.launch {
            getDataActionsInterface.getDishDataList(
                menuId = menuId,
                onDishDataList = { dishDataList ->
                    setDishList(dishDataList)
                    setUiState(ContentUiStates.Show)
                },
                onErrorMessage = { errorMessage ->
                    setUiIntent(ContentUiIntents.ShowSnackBarMsg(errorMessage))
                    setUiState(ContentUiStates.Error)
                }
            )
        }
//        viewModelScope.launch {
//            try {
//                val data = withContext(Dispatchers.IO) {
//                    downloadDataUseCaseInterface.downloadMenuDishListData(menuId = menuId)
//                }
//                setDishList(data)
//                setUiState(ContentUiStates.Show)
//            } catch (e: Exception) {
//                setUiState(ContentUiStates.Error)
//                setUiIntent(ContentUiIntents.ShowSnackBarMsg(e.message.toString()))
//            }
//        }
    }

    private fun createMenuId(menuId: String, userId: String) {
        setUiState(ContentUiStates.Loading)
        viewModelScope.launch {
            menuActionsInterface.createMenuId(
                menuId = menuId,
                userId = userId,
                onMenuId = { menuId ->
                    menuIdVM = menuId
                    setUiIntent(ContentUiIntents.GoToDishListScreen)
                },
                onErrorMessage = { message ->
                    setUiIntent(ContentUiIntents.ShowSnackBarMsg(message))
                    setUiState(ContentUiStates.Error)
                }
            )
        }
//        viewModelScope.launch {
//            try {
//                withContext(Dispatchers.IO) {
//                    uploadDataUseCaseInterface.uploadMenuId(
//                        userId = userId,
//                        menuIdFirebase = MenuIdFirebase(id = menuId),
//                    )
//                }
//                menuIdVM = menuId
//                setUiIntent(ContentUiIntents.GoToDishListScreen)
//            } catch (e: Exception) {
//                setUiState(ContentUiStates.Error)
//                setUiIntent(ContentUiIntents.ShowSnackBarMsg(e.message.toString()))
//            }
//        }
    }

//    private fun uploadDishItemData(
//        dishData: DishData,
//        menuId: String,
//    ) {
//        viewModelScope.launch {
//            try {
//                withContext(Dispatchers.IO) {
//                    uploadDataUseCaseInterface.uploadMenuDishData(
//                        data = dishData.toDishDataFirebase(),
//                        menuId = menuId,
//                        documentId = dishData.id,
//                    )
//                }
//            } catch (e: Exception) {
//                setUiIntent(ContentUiIntents.ShowSnackBarMsg(e.message.toString()))
//            }
//        }
//    }

    private fun setInitialImage(
        imageUri: Uri,
        dishData: DishData,
    ) {
        setUiState(ContentUiStates.Loading)
        viewModelScope.launch {
            editDishItemActionsInterface.setInitialImage(
                imageUri = imageUri,
                dishData = dishData,
                onUpdatedDish = { updatedDishData ->
                    setDishItemData(updatedDishData)
                    setUiState(ContentUiStates.Show)
                },
                onErrorMessage = { message ->
                    setUiState(ContentUiStates.Show)
                    setUiIntent(ContentUiIntents.ShowSnackBarMsg(message))
                }
            )
        }
//        viewModelScope.launch {
//            val bitmap = withContext(Dispatchers.Default) {
//                transformImageUseCaseInterface.getBitmapFromUri(imageUri)
//            }
//            val compressedBitmap = withContext(Dispatchers.Default) {
//                transformImageUseCaseInterface.compressBitmap(
//                    bitmap = bitmap,
//                    quality = 80,
//                )
//            }
//            val dishData = dishItemData.value.copy(updatedImageModel = compressedBitmap)
//            setDishItemData(dishData)
//            setUiState(ContentUiStates.Show)
//        }
    }

    private fun updateDishImage(imageBitmap: Bitmap, dishData: DishData) {
        setUiState(ContentUiStates.Loading)
        viewModelScope.launch {
            editDishItemActionsInterface.updateDishImage(
                imageBitmap = imageBitmap,
                dishData = dishData,
                onUpdatedDish = { updatedDishData ->
                    setDishItemData(updatedDishData)
                    setUiState(ContentUiStates.Show)
                },
                onErrorMessage = { errorMessage ->
                    setUiIntent(ContentUiIntents.ShowSnackBarMsg(errorMessage))
                    setUiState(ContentUiStates.Show)
                }
            )
//            try {
//                val bitmap = withContext(Dispatchers.Default) {
//                    transformBitmapImageInterface.segmentImageFromBitmap(imageBitmap)
//                }
//
//                val croppedBitmap = withContext(Dispatchers.Default) {
//                    transformBitmapImageInterface.cropBitmapToForeground(bitmap)
//                }
//
//                setDishItemData(dishItemData.value.copy(updatedImageModel = croppedBitmap))
//            } catch (e: Exception) {
//                setUiIntent(ContentUiIntents.ShowSnackBarMsg(e.message.toString()))
//            }
//            setUiState(ContentUiStates.Show)
        }
    }

    private fun saveDishItem(menuId: String, dishData: DishData, dishList: List<DishData>) {
        setUiState(ContentUiStates.Loading)
        viewModelScope.launch {
            editDishListActionsInterface.saveDishItem(
                menuId = menuId,
                dishData = dishData,
                dishList = dishList,
                onDishListUpdated = { newDishList ->
                    setDishList(newDishList)
                    setUiIntent(ContentUiIntents.ShowSnackBarMsg("The dish was saved"))
                    setUiState(ContentUiStates.Show)
                },
                onErrorMessage = { errorMessage ->
                    setUiIntent(ContentUiIntents.ShowSnackBarMsg(errorMessage))
                    setUiState(ContentUiStates.Show)
                },
            )
//            try {
//                dishData.updatedImageModel?.let { bitmap ->
//                    val byteArrayOfBitmapImage =
//                        withContext(Dispatchers.Default) { //transforming image to byte array
//                            transformImageUseCaseInterface.getByteArrayFromBitmap(bitmap = bitmap)
//                        }
//
//                    withContext(Dispatchers.IO) { //uploading image to firebase
//                        uploadFileUseCaseInterface.uploadDishPictureUsingByteArray(
//                            menuId = menuId,
//                            dishId = dishData.id,
//                            byteArray = byteArrayOfBitmapImage,
//                        )
//                    }
//
//                    val uri = withContext(Dispatchers.IO) { //receiving uri of uploaded image
//                        downloadFileUseCaseInterface.downloadUriOfDishPicture(
//                            menuId = menuId,
//                            dishId = dishData.id,
//                        )
//                    }
//
//                    withContext(Dispatchers.IO) { //uploading the data of the dish to firestore
//                        uploadDishItemData(
//                            dishData = dishData.copy(imageModel = uri),
//                            menuId = menuId,
//                        )
//                    }
//
//                    val newDishList =
//                        withContext(Dispatchers.IO) { //add the data of the dish to dish list
//                            saveDishItemInList(dishData.copy(imageModel = uri))
//                        }
//                    setDishList(newDishList)
//                }
//
//                if (dishData.updatedImageModel == null) {
//                    withContext(Dispatchers.IO) {
//                        uploadDishItemData(
//                            dishData = dishData,
//                            menuId = menuId,
//                        )
//                    }
//
//                    val newDishList = withContext(Dispatchers.IO) {
//                        saveDishItemInList(dishData)
//                    }
//                    setDishList(newDishList)
//                }
//                setUiIntent(ContentUiIntents.ShowSnackBarMsg("The dish was saved"))
//            } catch (e: Exception) {
//                setUiIntent(ContentUiIntents.ShowSnackBarMsg(e.message.toString()))
//            }
//            setUiState(ContentUiStates.Show)
        }
    }

    private fun generateDishDescription(
        dishImage: Bitmap,
        dishData: DishData,
    ) {
        setUiState(ContentUiStates.Loading)
        viewModelScope.launch {
            editDishItemActionsInterface.generateDishDescription(
                dishImage = dishImage,
                dishData = dishData,
                onUpdatedDish = { updatedDishData ->
                    setDishItemData(updatedDishData)
                    setUiIntent(ContentUiIntents.ShowSnackBarMsg("The description was generated"))
                    setUiState(ContentUiStates.Show)
                },
                onErrorMessage = { errorMessage ->
                    setUiIntent(ContentUiIntents.ShowSnackBarMsg(errorMessage))
                    setUiState(ContentUiStates.Show)
                }

            )
        }
//        viewModelScope.launch() {
//            try {
//                val response = withContext(Dispatchers.IO) {
//                    generateAiTextUseCaseInterface.generateAiDescriptionOfDish(
//                        imageBitmap = dishImage,
//                        dishName = name,
//                    )
//                }
//                val dishItem: DishData = dishItemData.value.copy(description = response)
//                setDishItemData(dishItem)
//            } catch (e: Exception) {
//                setUiIntent(ContentUiIntents.ShowSnackBarMsg(e.message.toString()))
//            }
//            setUiState(ContentUiStates.Show)
//        }
    }

    private fun createMenuDocFile(menuId: String, folderUri: Uri, dishList: List<DishData>) {
        setUiState(ContentUiStates.Loading)
        viewModelScope.launch {
            pdfFileActionsInterface.createMenuPdfFile(
                menuId = menuId,
                folderUri = folderUri,
                dishList = dishList,
                onSuccess = { message ->
                    setUiIntent(ContentUiIntents.ShowSnackBarMsg(message))
                    setUiState(ContentUiStates.Show)
                },
                onErrorMessage = { message ->
                    setUiIntent(ContentUiIntents.ShowSnackBarMsg(message))
                    setUiState(ContentUiStates.Show)
                }
            )
        }
//        viewModelScope.launch {
//            try {
//                val pdfDishList = mutableListOf<PdfDishData>()
//
//                for (dish in dishList) {
//                    var imageBitmap: Bitmap? = null
//
//                    if (dish.imageModel != null) {
//                        val imageByteArray: ByteArray =
//                            withContext(Dispatchers.IO) {
//                                downloadFileUseCaseInterface.downloadDishImageFile(
//                                    menuId = menuId,
//                                    dishId = dish.id,
//                                )
//                            }
//
//                        imageBitmap = withContext(Dispatchers.IO) {
//                            transformImageUseCaseInterface.getBitmapFromByteArray(imageByteArray)
//                        }
//                    }
//
//                    pdfDishList.add(
//                        PdfDishData(
//                            name = dish.name,
//                            price = dish.price,
//                            weight = dish.weight,
//                            description = dish.description,
//                            bitmap = imageBitmap,
//                        )
//                    )
//                }
//                withContext(Dispatchers.IO) {
//                    saveMenuPdfFileUseCaseInterface.saveMenuPdfFileInFolder(
//                        folderUri = folderUri,
//                        pdfDishList = pdfDishList,
//                    )
////                    createDicFileInterface.createMenuDoc(
////                        folderUri = folderUri,
////                        dishList = docDishList,
////                    )
//                }
//                setUiIntent(ContentUiIntents.ShowSnackBarMsg("DOC file was created"))
//            } catch (e: Exception) {
//                setUiIntent(ContentUiIntents.ShowSnackBarMsg(e.message.toString()))
//            }
//            setUiState(ContentUiStates.Show)
//        }
    }

    private fun createTranslatedMenuDocFile(
        menuId: String,
        folderUri: Uri,
        dishList: List<DishData>,
        translateLanguage: String
    ) {
        setUiState(ContentUiStates.Loading)
        viewModelScope.launch {
            pdfFileActionsInterface.createTranslatedMenuPdfFile(
                menuId = menuId,
                folderUri = folderUri,
                translateLanguage = translateLanguage,
                dishList = dishList,
                onSuccess = { message ->
                    setUiIntent(ContentUiIntents.ShowSnackBarMsg(message))
                    setUiState(ContentUiStates.Show)
                },
                onErrorMessage = { message ->
                    setUiIntent(ContentUiIntents.ShowSnackBarMsg(message))
                    setUiState(ContentUiStates.Show)
                }
            )
        }
//        viewModelScope.launch {
//            try {
//                val docDishList = mutableListOf<DocDishData>()
//
//                for (dish in dishList.value) {
//                    val translatedName = withContext(Dispatchers.IO) {
//                        generateAiTextUseCaseInterface.translateText(
//                            text = dish.name,
//                            language = translateLanguage,
//                        )
//                    }
//
//                    val translatedDescription = withContext(Dispatchers.IO) {
//                        generateAiTextUseCaseInterface.translateText(
//                            text = dish.description,
//                            language = translateLanguage,
//                        )
//                    }
//
//                    val imageByteArray = withContext(Dispatchers.IO) {
//                        downloadFileUseCaseInterface.downloadDishImageFile(
//                            menuId = menuId,
//                            dishId = dish.id,
//                        )
//                    }
//
//                    docDishList.add(
//                        DocDishData(
//                            name = translatedName,
//                            price = dish.price,
//                            weight = dish.weight,
//                            description = translatedDescription,
//                            imageByteArray = imageByteArray,
//                        )
//                    )
//                }
//                withContext(Dispatchers.IO) {
//                    createDicFileInterface.createMenuDoc(
//                        folderUri = folderUri,
//                        dishList = docDishList,
//                        language = translateLanguage,
//                    )
//                }
//                setUiIntent(ContentUiIntents.ShowSnackBarMsg("DOC file was created"))
//            } catch (e: Exception) {
//                setUiIntent(ContentUiIntents.ShowSnackBarMsg(e.message.toString()))
//            }
//            setUiState(ContentUiStates.Show)
//        }
    }

    private fun deleteDish(menuId: String, dishId: String, dishList: List<DishData>) {
        setUiState(ContentUiStates.Loading)
        viewModelScope.launch {
            editDishListActionsInterface.deleteDishItem(
                menuId = menuId,
                dishId = dishId,
                dishList = dishList,
                onDishListUpdated = { newDishList ->
                    setDishList(newDishList)
                    setUiIntent(ContentUiIntents.ShowSnackBarMsg("The dish was deleted"))
                    setUiState(ContentUiStates.Show)
                },
                onErrorMessage = { errorMessage ->
                    setUiIntent(ContentUiIntents.ShowSnackBarMsg(errorMessage))
                    setUiState(ContentUiStates.Show)
                }
            )
//            try {
//                val dishDeleted = async(Dispatchers.IO) {
//                    deleteDataUseCaseInterface.deleteMenuDish(
//                        menuId = menuId,
//                        dishId = dishId,
//                    )
//                }
//
//                val isImageDishExisted = withContext(Dispatchers.IO) {
//                    downloadFileUseCaseInterface.checkIfDishImageExists(
//                        menuId = menuId,
//                        dishId = dishId
//                    )
//                }
//
//                val imageDeleted = async(Dispatchers.IO) {
//                    if (isImageDishExisted)
//                        deleteFileUseCaseInterface.deleteDish(
//                            menuId = menuId,
//                            dishId = dishId,
//                        )
//                }
//
//
//                imageDeleted.await()
//                dishDeleted.await()
//                val newDishList = withContext(Dispatchers.IO) {
//                    deleteDishItemFromList(dishId)
//                }
//                setDishList(newDishList)
//            } catch (e: Exception) {
//                setUiIntent(ContentUiIntents.ShowSnackBarMsg(e.message.toString()))
//            }
        }
//        setUiState(ContentUiStates.Show)
    }

//    private suspend fun deleteDishItemFromList(dishId: String): List<DishData> {
//        return suspendCoroutine { continuation ->
//            val newDishList = dishList.value.toMutableList()
//            newDishList.removeIf { it.id == dishId }
//            continuation.resume(newDishList)
//        }
//    }
//
//    private suspend fun saveDishItemInList(dishData: DishData): List<DishData> {
//        return suspendCoroutine { continuation ->
//            val dishList = dishList.value
//            val newDishList = mutableMapOf<String, DishData>()
//            for (dish in dishList) {
//                newDishList[dish.id] = dish
//            }
//            newDishList[dishData.id] = dishData
//            continuation.resume(newDishList.values.toList())
//        }
//    }

}