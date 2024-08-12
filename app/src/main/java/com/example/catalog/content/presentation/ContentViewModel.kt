package com.example.catalog.content.presentation

import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.viewModelScope
import com.example.catalog.content.domain.data.DishData
import com.example.catalog.content.domain.data.DocDishData
import com.example.catalog.content.domain.data.MenuIdFirebase
import com.example.catalog.content.domain.data.toDishDataFirebase
import com.example.catalog.content.domain.functions.CreateDocFileInterface
import com.example.catalog.content.domain.functions.TransformBitmapImageInterface
import com.example.catalog.content.domain.interfaces.DeleteDataUseCaseInterface
import com.example.catalog.content.domain.interfaces.DeleteFileUseCaseInterface
import com.example.catalog.content.domain.interfaces.DownloadDataUseCaseInterface
import com.example.catalog.content.domain.interfaces.DownloadFileUseCaseInterface
import com.example.catalog.content.domain.interfaces.GenerateAiTextUseCaseInterface
import com.example.catalog.content.domain.interfaces.TransformImageUseCaseInterface
import com.example.catalog.content.domain.interfaces.UploadDataUseCaseInterface
import com.example.catalog.content.domain.interfaces.UploadFileUseCaseInterface
import com.example.catalog.content.presentation.base.BaseViewModel
import com.example.catalog.login.domain.interfaces.PhoneAuthUseCaseInterface
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.UUID
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@HiltViewModel
class ContentViewModel @Inject constructor(
    private val downloadDataUseCaseInterface: DownloadDataUseCaseInterface,
    private val uploadDataUseCaseInterface: UploadDataUseCaseInterface,
    private val phoneAuthUseCaseInterface: PhoneAuthUseCaseInterface,
    private val uploadFileUseCaseInterface: UploadFileUseCaseInterface,
    private val downloadFileUseCaseInterface: DownloadFileUseCaseInterface,
    private val generateAiTextUseCaseInterface: GenerateAiTextUseCaseInterface,
    private val transformImageUseCaseInterface: TransformImageUseCaseInterface,
    private val transformBitmapImageInterface: TransformBitmapImageInterface,
    private val createDicFileInterface: CreateDocFileInterface,
    private val deleteDataUseCaseInterface: DeleteDataUseCaseInterface,
    private val deleteFileUseCaseInterface: DeleteFileUseCaseInterface,
) : BaseViewModel<ContentUiStates, ContentUiIntents, ContentUiEvents>(ContentUiStates.Loading) {

    private var menuIdVM: String? = null
    private var userIdVM: String? = null

    private val dishItemData = MutableStateFlow<DishData>(DishData())
    private val dishItemDataFlow = dishItemData.asStateFlow()

    private val dishList = MutableStateFlow<List<DishData>>(emptyList())
    private val dishListFlow = dishList.asStateFlow()

    private fun setDishItemData(data: DishData) {
        dishItemData.value = data
    }

    private fun setDishList(list: List<DishData>) {
        dishList.value = list
    }

    fun getDishItemDataFlow() = dishItemDataFlow
    fun getDishListFlow() = dishListFlow

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
                if (menuIdVM == null) {
                    checkMenuId()
                }
                menuIdVM?.let { id ->
                    getDishDataList(menuId = id)
                }
            }

            is ContentUiEvents.SaveDishItem -> {
                menuIdVM?.let { id ->
                    saveDishItem(
                        menuId = id,
                        dishData = uiEvent.dishData,
                    )
                }
                if (menuIdVM == null)
                    checkMenuId()
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
                }
                if (userIdVM == null)
                    checkMenuId()
            }

            is ContentUiEvents.CheckMenuId -> {
                checkMenuId()
            }

            is ContentUiEvents.GenerateDescriptionOfDish -> {
                generateDishDescription(
                    dishImage = uiEvent.imageBitmap,
                    name = uiEvent.dishName,
                )
            }

            is ContentUiEvents.CreateUpdatedImage -> {
                updateDishImage(uiEvent.imageBitmap)
            }


            is ContentUiEvents.SetInitialImage -> {
                viewModelScope.launch {
                    setUiState(ContentUiStates.Loading)
                    val bitmap = withContext(Dispatchers.Default) {
                        transformImageUseCaseInterface.getBitmapFromUri(uiEvent.imageUri)
                    }
                    val compressedBitmap = withContext(Dispatchers.Default) {
                        transformImageUseCaseInterface.compressBitmap(
                            bitmap = bitmap,
                            quality = 80,
                        )
                    }
                    val dishData = dishItemData.value.copy(updatedImageModel = compressedBitmap)
                    setDishItemData(dishData)
                    setUiState(ContentUiStates.Show)
                }
            }

            is ContentUiEvents.SaveMenuAsDocFile -> {
                menuIdVM?.let { id ->
                    if (uiEvent.language != null) {
                        createTranslatedMenuDocFile(
                            menuId = id,
                            folderUri = uiEvent.folderUri,
                            translateLanguage = uiEvent.language,
                        )
                    } else
                        createMenuDocFile(
                            menuId = id,
                            uiEvent.folderUri,
                        )
                }
                if (menuIdVM == null)
                    checkMenuId()
            }

            is ContentUiEvents.SetNamePriceWeightDescription -> {
                setDishItemData(
                    dishItemData.value.copy(
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
                    )
                }
                if (menuIdVM == null)
                    checkMenuId()
            }
        }
    }

    private fun checkMenuId() {
        setUiState(ContentUiStates.Loading)
        userIdVM?.let { id ->
            getCurrentMenuId(id)
        }
        if (userIdVM == null)
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

    private fun getCurrentMenuId(userId: String) {
        setUiState(ContentUiStates.Loading)
        viewModelScope.launch {
            try {
                val data = withContext(Dispatchers.IO) {
                    downloadDataUseCaseInterface.downloadMenuId(userId = userId)
                }
                if (data == null) {
                    setUiState(ContentUiStates.Show)
                }
                else {
                    menuIdVM = data.id
                    setUiIntent(ContentUiIntents.GoToMenuListScreen)
                }
            } catch (e: Exception) {
                setUiState(ContentUiStates.Error)
                setUiIntent(ContentUiIntents.ShowSnackBarMsg(e.message.toString()))
            }
        }
    }

    private fun getDishDataList(menuId: String) {
        setUiState(ContentUiStates.Loading)
        viewModelScope.launch {
            try {
                val data = withContext(Dispatchers.IO) {
                    downloadDataUseCaseInterface.downloadMenuDishListData(menuId = menuId)
                }
                setDishList(data)
                setUiState(ContentUiStates.Show)
            } catch (e: Exception) {
                setUiState(ContentUiStates.Error)
                setUiIntent(ContentUiIntents.ShowSnackBarMsg(e.message.toString()))
            }
        }
    }

    private fun createMenuId(menuId: String, userId: String) {
        setUiState(ContentUiStates.Loading)
        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    uploadDataUseCaseInterface.uploadMenuId(
                        userId = userId,
                        menuIdFirebase = MenuIdFirebase(id = menuId),
                    )
                }
                menuIdVM = menuId
                setUiIntent(ContentUiIntents.GoToMenuListScreen)
            } catch (e: Exception) {
                setUiState(ContentUiStates.Error)
                setUiIntent(ContentUiIntents.ShowSnackBarMsg(e.message.toString()))
            }
        }
    }

    private fun uploadDishItemData(
        dishData: DishData,
        menuId: String,
    ) {
        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    uploadDataUseCaseInterface.uploadMenuDishData(
                        data = dishData.toDishDataFirebase(),
                        menuId = menuId,
                        documentId = dishData.id,
                    )
                }
            } catch (e: Exception) {
                setUiIntent(ContentUiIntents.ShowSnackBarMsg(e.message.toString()))
            }
        }
    }

    private fun updateDishImage(imageBitmap: Bitmap) {
        viewModelScope.launch {
            setUiState(ContentUiStates.Loading)
            try {
                val bitmap = withContext(Dispatchers.Default) {
                    transformBitmapImageInterface.segmentImageFromBitmap(imageBitmap)
                }

                val croppedBitmap = withContext(Dispatchers.Default) {
                    transformBitmapImageInterface.cropBitmapToForeground(bitmap)
                }

                setDishItemData(dishItemData.value.copy(updatedImageModel = croppedBitmap))
            } catch (e: Exception) {
                setUiIntent(ContentUiIntents.ShowSnackBarMsg(e.message.toString()))
            }
            setUiState(ContentUiStates.Show)
        }
    }

    private fun saveDishItem(menuId: String, dishData: DishData) {
        setUiState(ContentUiStates.Loading)
        viewModelScope.launch {
            try {
                dishData.updatedImageModel?.let { bitmap ->
                    val byteArray = withContext(Dispatchers.Default) {
                        transformImageUseCaseInterface.getByteArrayFromBitmap(bitmap = bitmap)
                    }

                    withContext(Dispatchers.IO) {
                        uploadFileUseCaseInterface.uploadDishPictureUsingByteArray(
                            menuId = menuId,
                            dishId = dishData.id,
                            byteArray = byteArray,
                        )
                    }

                    val uri = withContext(Dispatchers.IO) {
                        downloadFileUseCaseInterface.downloadUriOfDishPicture(
                            menuId = menuId,
                            dishId = dishData.id,
                        )
                    }

                    uploadDishItemData(
                        dishData = dishData.copy(imageModel = uri),
                        menuId = menuId,
                    )

                    val newDishList = withContext(Dispatchers.IO) {
                        saveDishItemInList(dishData.copy(imageModel = uri))
                    }
                    setDishList(newDishList)
                }

                if (dishData.updatedImageModel == null) {
                    withContext(Dispatchers.IO) {
                        uploadDishItemData(
                            dishData = dishData,
                            menuId = menuId,
                        )
                    }

                    val newDishList = withContext(Dispatchers.IO) {
                        saveDishItemInList(dishData)
                    }
                    setDishList(newDishList)
                }
                setUiIntent(ContentUiIntents.ShowSnackBarMsg("The dish was saved"))
            } catch (e: Exception) {
                setUiIntent(ContentUiIntents.ShowSnackBarMsg(e.message.toString()))
            }
            setUiState(ContentUiStates.Show)
        }
    }

    private fun generateDishDescription(dishImage: Bitmap, name: String) {
        setUiState(ContentUiStates.Loading)
        viewModelScope.launch() {
            try {
                val response = withContext(Dispatchers.IO) {
                    generateAiTextUseCaseInterface.generateAiDescriptionOfDish(
                        imageBitmap = dishImage,
                        dishName = name,
                    )
                }
                val dishItem: DishData = dishItemData.value.copy(description = response)
                setDishItemData(dishItem)
            } catch (e: Exception) {
                setUiIntent(ContentUiIntents.ShowSnackBarMsg(e.message.toString()))
            }
            setUiState(ContentUiStates.Show)
        }
    }

    private fun createMenuDocFile(menuId: String, folderUri: Uri) {
        setUiState(ContentUiStates.Loading)
        viewModelScope.launch {
            try {
                val docDishList = mutableListOf<DocDishData>()

                for (dish in dishList.value) {
                    val imageByteArray = withContext(Dispatchers.IO) {
                        downloadFileUseCaseInterface.downloadDishImageFile(
                            menuId = menuId,
                            dishId = dish.id,
                        )
                    }

                    docDishList.add(
                        DocDishData(
                            name = dish.name,
                            price = dish.price,
                            weight = dish.weight,
                            description = dish.description,
                            imageByteArray = imageByteArray,
                        )
                    )
                }
                withContext(Dispatchers.IO) {
                    createDicFileInterface.createMenuDoc(
                        folderUri = folderUri,
                        dishList = docDishList,
                    )
                }
                setUiIntent(ContentUiIntents.ShowSnackBarMsg("DOC file was created"))
            } catch (e: Exception) {
                setUiIntent(ContentUiIntents.ShowSnackBarMsg(e.message.toString()))
            }
            setUiState(ContentUiStates.Show)
        }
    }

    private fun createTranslatedMenuDocFile(
        menuId: String,
        folderUri: Uri,
        translateLanguage: String
    ) {
        setUiState(ContentUiStates.Loading)
        viewModelScope.launch {
            try {
                val docDishList = mutableListOf<DocDishData>()

                for (dish in dishList.value) {
                    val translatedName = withContext(Dispatchers.IO) {
                        generateAiTextUseCaseInterface.translateText(
                            text = dish.name,
                            language = translateLanguage,
                        )
                    }

                    val translatedDescription = withContext(Dispatchers.IO) {
                        generateAiTextUseCaseInterface.translateText(
                            text = dish.description,
                            language = translateLanguage,
                        )
                    }

                    val imageByteArray = withContext(Dispatchers.IO) {
                        downloadFileUseCaseInterface.downloadDishImageFile(
                            menuId = menuId,
                            dishId = dish.id,
                        )
                    }

                    docDishList.add(
                        DocDishData(
                            name = translatedName,
                            price = dish.price,
                            weight = dish.weight,
                            description = translatedDescription,
                            imageByteArray = imageByteArray,
                        )
                    )
                }
                withContext(Dispatchers.IO) {
                    createDicFileInterface.createMenuDoc(
                        folderUri = folderUri,
                        dishList = docDishList,
                        language = translateLanguage,
                    )
                }
                setUiIntent(ContentUiIntents.ShowSnackBarMsg("DOC file was created"))
            } catch (e: Exception) {
                setUiIntent(ContentUiIntents.ShowSnackBarMsg(e.message.toString()))
            }
            setUiState(ContentUiStates.Show)
        }
    }

    private fun deleteDish(menuId: String, dishId: String) {
        setUiState(ContentUiStates.Loading)
        viewModelScope.launch {
            try {
                val dishDeleted = async(Dispatchers.IO) {
                    deleteDataUseCaseInterface.deleteMenuDish(
                        menuId = menuId,
                        dishId = dishId,
                    )
                }

                val isImageDishExisted = withContext(Dispatchers.IO) {
                    downloadFileUseCaseInterface.checkIfDishImageExists(
                        menuId = menuId,
                        dishId = dishId
                    )
                }

                val imageDeleted = async(Dispatchers.IO) {
                    if (isImageDishExisted)
                        deleteFileUseCaseInterface.deleteDish(
                            menuId = menuId,
                            dishId = dishId,
                        )
                }


                imageDeleted.await()
                dishDeleted.await()
                val newDishList = withContext(Dispatchers.IO) {
                    deleteDishItemFromList(dishId)
                }
                setDishList(newDishList)
            } catch (e: Exception) {
                setUiIntent(ContentUiIntents.ShowSnackBarMsg(e.message.toString()))
            }
        }
        setUiState(ContentUiStates.Show)
    }

    private suspend fun deleteDishItemFromList(dishId: String): List<DishData> {
        return suspendCoroutine { continuation ->
            val newDishList = dishList.value.toMutableList()
            newDishList.removeIf { it.id == dishId }
            continuation.resume(newDishList)
        }
    }

    private suspend fun saveDishItemInList(dishData: DishData): List<DishData> {
        return suspendCoroutine { continuation ->
            val dishList = dishList.value
            val newDishList = mutableMapOf<String, DishData>()
            for (dish in dishList) {
                newDishList[dish.id] = dish
            }
            newDishList[dishData.id] = dishData
            continuation.resume(newDishList.values.toList())
        }
    }

}