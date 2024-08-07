package com.example.catalog.content.presentation

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.catalog.content.data.repositories.ImageSegmentationRepository
import com.example.catalog.content.domain.data.DishData
import com.example.catalog.content.domain.data.DishDataFirebase
import com.example.catalog.content.domain.data.DocDishData
import com.example.catalog.content.domain.data.MenuIdFirebase
import com.example.catalog.content.domain.data.toDishDataFirebase
import com.example.catalog.content.domain.functions.CreateDocFileInterface
import com.example.catalog.content.domain.functions.TransformBitmapImageInterface
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
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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
    private val transformBitmapImageInterface: TransformBitmapImageInterface,
    private val createDicFileInterface: CreateDocFileInterface,
    @ApplicationContext private val context: Context,

    private val imageSegmentationRepository: ImageSegmentationRepository
) : BaseViewModel<ContentUiStates, ContentUiIntents, ContentUiEvents>(ContentUiStates.Show) {

    private var menuIdVM: String? = null
    private var userIdVM: String? = null

    private val dishItemData = MutableStateFlow<DishData>(DishData())
    private val dishItemDataFlow = dishItemData.asStateFlow()

    private val dishList = MutableStateFlow<List<DishData>>(emptyList())
    private val dishListFlow = dishList.asStateFlow()

    private val imageList = MutableStateFlow<List<Bitmap>>(emptyList())
    private val imageListFlow = imageList.asStateFlow()
    fun getImageListFlow() = imageListFlow

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

            }

            is ContentUiEvents.EditDishItem -> {
                setDishItemData(data = uiEvent.dishData)
                setUiIntent(ContentUiIntents.GoToEditDishScreen)
            }

            is ContentUiEvents.DownloadMenuList -> {
                menuIdVM?.let { id ->
                    getDishDataList(menuId = id)
                }
            }

            is ContentUiEvents.SaveDishItem -> {
                saveDishItem(dishData = uiEvent.dishData)
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

            is ContentUiEvents.SetNewDishImageFromGallery -> {
                setUiIntent(ContentUiIntents.GoToUpdateDishImageScreen(uiEvent.imageUri.toString()))
            }

            is ContentUiEvents.CreateUpdatedImage -> {
                setUiState(ContentUiStates.Loading)
                updateDishImage(uiEvent.imageBitmap)
            }

            is ContentUiEvents.SegmentImage -> {
//                setUiState(ContentUiStates.Loading)
//                imageSegmentationRepository.segmentImage(
//                    uri = uiEvent.imageUri,
//                    onResult = {
//                        imageList.value = it
//                        setUiState(ContentUiStates.Show)
//                    },
//                    onError = {
//                        setUiState(ContentUiStates.Error)
//                    }
//                )
            }

            is ContentUiEvents.SetInitialImage -> {
                viewModelScope.launch {
                    setUiState(ContentUiStates.Loading)
                    val bitmap = withContext(Dispatchers.Default) {
                        transformImageUseCaseInterface.getBitmapFromUri(uiEvent.imageUri)
                    }
                    val dishData = dishItemData.value.copy(updatedImageModel = bitmap)
                    setDishItemData(dishData)
                    Log.d("DAVAI", dishItemData.value.toString())
                    setUiState(ContentUiStates.Show)
                }
            }

            is ContentUiEvents.SetUpdatedImage -> {
//                setUiState(ContentUiStates.Loading)
//                setDishItemData(dishItemData.value.copy(updatedImageModel = uiEvent.bitmap))
//                setUiState(ContentUiStates.Show)
            }

            is ContentUiEvents.SaveMenuAsDocFile -> {
                if (uiEvent.language != null)
                    createTranslatedMenuDocFile(
                        folderUri = uiEvent.folderUri,
                        translateLanguage = uiEvent.language,
                    )
                else
                    createMenuDocFile(uiEvent.folderUri)
            }

            is ContentUiEvents.SetNamePriceWeightDescription -> {
                setDishItemData(dishItemData.value.copy(
                    name = uiEvent.name,
                    price = uiEvent.price.toDouble(),
                    weight = uiEvent.weight.toDouble(),
                    description = uiEvent.description
                ))
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
                    Log.d("DAVAI", "Current user is ${firebaseUser.uid}")
                    userIdVM = firebaseUser.uid
                    getCurrentMenuId(firebaseUser.uid)
                },
                onUserDoesNotExist = {
                    Log.d("DAVAI", "Get current user error")
                    setUiState(ContentUiStates.Error)
                }
            )
    }

    private fun getCurrentMenuId(userId: String) {
        viewModelScope.launch {
            try {
                val data = withContext(Dispatchers.IO) {
                    downloadDataUseCaseInterface.downloadMenuId(userId = userId)
                }
                if (data == null)
                    setUiState(ContentUiStates.Error)
                else {
                    menuIdVM = data.id
                    setUiIntent(ContentUiIntents.GoToMenuListScreen)
                }
            } catch (e: Exception) {
                setUiState(ContentUiStates.Error)
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
                setUiState(ContentUiStates.Show)
            } catch (e: Exception) {
                setUiState(ContentUiStates.Error)
            }
        }
    }

    private fun uploadImageAndSaveDishItemData(
        dishDataFirebase: DishDataFirebase,
        imageByteArray: ByteArray,
        menuId: String
    ) {
        setUiState(ContentUiStates.Loading)
        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    uploadFileUseCaseInterface.uploadDishPictureUsingByteArray(
                        menuId = menuId,
                        dishId = dishDataFirebase.id,
                        byteArray = imageByteArray,
                    )
                }
                val uri = withContext(Dispatchers.IO) {
                    downloadFileUseCaseInterface.downloadUriOfDishPicture(
                        menuId = menuId,
                        dishId = dishDataFirebase.id,
                    )
                }
                uploadDishItemData(
                    dishDataFirebase = dishDataFirebase.copy(image = uri.toString()),
                    menuId = menuId,
                )
            } catch (e: Exception) {
                setUiState(ContentUiStates.Error)
            }
        }
    }

    private fun uploadDishItemData(
        dishDataFirebase: DishDataFirebase,
        menuId: String,
    ) {
        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    uploadDataUseCaseInterface.uploadMenuDishData(
                        data = dishDataFirebase,
                        menuId = menuId,
                        documentId = dishDataFirebase.id,
                    )
                }
            } catch (e: Exception) {
                setUiState(ContentUiStates.Error)
            }
        }
    }

    private fun updateDishImage(imageBitmap: Bitmap) {
        viewModelScope.launch {
            try {
                Log.d("DAVAI", "fun1")
                val bitmap = withContext(Dispatchers.Default) {
                    transformBitmapImageInterface.segmentImageFromBitmap(imageBitmap)
                }
                Log.d("DAVAI", "fun2")

                val croppedBitmap = withContext(Dispatchers.Default) {
                    transformBitmapImageInterface.cropBitmapToForeground(bitmap)
                }

//                val bitmapWithBackground = color?.let {
//                    Log.d("DAVAI", "fun3")
//                    withContext(Dispatchers.Default) {
//                        transformBitmapImageInterface.addBackgroundToBitmap(
//                            croppedBitmap,
//                            it.toArgb()
//                        )
//                    }
//                } ?: croppedBitmap

                setDishItemData(dishItemData.value.copy(updatedImageModel = croppedBitmap))
                setUiState(ContentUiStates.Show)
                Log.d("DAVAI", "fun5")
            } catch (e: Exception) {
                setUiState(ContentUiStates.Error)
            }
        }
    }

    private fun saveDishItem(dishData: DishData) {
        viewModelScope.launch {
            menuIdVM?.let { id ->
                dishData.updatedImageModel?.let { bitmap ->
                    val byteArray = withContext(Dispatchers.Default) {
                        transformImageUseCaseInterface.getByteArrayFromBitmapWithQuality(
                            bitmap = bitmap,
                            quality = 50,
                        )
                    }
                    withContext(Dispatchers.IO) {
                        uploadImageAndSaveDishItemData(
                            dishDataFirebase = dishData.toDishDataFirebase(),
                            imageByteArray = byteArray,
                            menuId = id,
                        )
                    }
                }
                if (dishData.updatedImageModel == null) {
                    withContext(Dispatchers.IO) {
                        uploadDishItemData(
                            dishDataFirebase = dishData.toDishDataFirebase(),
                            menuId = id,
                        )
                    }
                }
                setUiState(ContentUiStates.Show)
                setUiIntent(ContentUiIntents.GoToMenuListScreen)
            }
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
                Log.d("DAVAI", "AI dish item: $dishItem")
                setDishItemData(dishItem)
            } catch (e: Exception) {
                setUiState(ContentUiStates.Error)
            }
            setUiState(ContentUiStates.Show)
        }
    }

    private fun createMenuDocFile(folderUri: Uri) {
        setUiState(ContentUiStates.Loading)
        viewModelScope.launch {
            try {
                menuIdVM?.let { id ->
                    val docDishList = mutableListOf<DocDishData>()
                    for (dish in dishList.value) {
                        Log.d("DAVAI", dish.name)

                        val imageByteArray = withContext(Dispatchers.IO) {
                            downloadFileUseCaseInterface.downloadDishImageFile(
                                menuId = id,
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
                            context = context,
                        )
                    }
                }
            } catch (e: Exception) {
                Log.d("DAVAI", "exception VM $e")
                setUiState(ContentUiStates.Error)
            }
            setUiState(ContentUiStates.Show)
        }
    }

    private fun createTranslatedMenuDocFile(folderUri: Uri, translateLanguage: String) {
        setUiState(ContentUiStates.Loading)
        viewModelScope.launch {
            try {
                menuIdVM?.let { id ->
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
                                text = dish.name,
                                language = translateLanguage,
                            )
                        }

                        val imageByteArray = withContext(Dispatchers.IO) {
                            downloadFileUseCaseInterface.downloadDishImageFile(
                                menuId = id,
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
                            context = context,
                        )
                    }
                }
            } catch (e: Exception) {
                Log.d("DAVAI", "exception VM $e")
                setUiState(ContentUiStates.Error)
            }
            setUiState(ContentUiStates.Show)
        }
    }

}