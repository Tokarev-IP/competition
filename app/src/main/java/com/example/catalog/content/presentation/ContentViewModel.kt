package com.example.catalog.content.presentation

import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.catalog.content.domain.data.DishData
import com.example.catalog.content.domain.data.MenuId
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
) : BaseViewModel<ContentUiStates, ContentUiIntents, ContentUiEvents>(ContentUiStates.Show) {

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
                menuIdVM?.let { id ->
                    uiEvent.imageUri?.let { imageUri ->
                        val byteArray =
                            transformImageUseCaseInterface.getByteArrayFromUriHighQuality(imageUri)
                        uploadImageAndSaveDishData(
                            dishData = uiEvent.dishData,
                            imageByteArray = byteArray,
                            menuId = id,
                        )
                    }

                    uploadDishItemData(
                        dishData = uiEvent.dishData,
                        menuId = id,
                        onSuccess = {
                            setUiState(ContentUiStates.Show)
                            setUiIntent(ContentUiIntents.GoToMenuListScreen)
                        }
                    )
                }
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

            is ContentUiEvents.GenerateTextUsingGemini -> {
                setUiState(ContentUiStates.Loading)
                val imageBitmap =
                    transformImageUseCaseInterface.getBitmapFromUriHighQuality(uiEvent.uri)
                generateText(
                    imageBitmap = imageBitmap,
                    parameters = uiEvent.parameters,
                )
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
        downloadDataUseCaseInterface.downloadMenuId(
            userId = userId,
            onSuccess = { menuId: MenuId ->
                menuIdVM = menuId.id
                setUiIntent(ContentUiIntents.GoToMenuListScreen)
            },
            onNull = {
                setUiState(ContentUiStates.Show)
            },
            onFailure = {
                Log.d("DAVAI", "Get current menu Id error $it")
                setUiState(ContentUiStates.Error)
            },
        )
    }

    private fun getDishDataList(menuId: String) {
        setUiState(ContentUiStates.Loading)
        downloadDataUseCaseInterface.downloadMenuDishListData(
            menuId = menuId,
            onSuccess = { dishList ->
                setDishList(dishList)
                setUiState(ContentUiStates.Show)
            },
            onFailure = {
                setUiState(ContentUiStates.Show)
            },
        )
    }

    private fun createMenuId(menuId: String, userId: String) {
        setUiState(ContentUiStates.Loading)
        uploadDataUseCaseInterface.uploadMenuId(
            userId = userId,
            menuId = MenuId(id = menuId),
            onSuccess = {
                setUiState(ContentUiStates.Show)
                menuIdVM = menuId
                setUiIntent(ContentUiIntents.GoToMenuListScreen)
            },
            onFailure = {
                setUiState(ContentUiStates.Show)
            }
        )
    }

    private fun uploadImageAndSaveDishData(
        dishData: DishData,
        imageByteArray: ByteArray,
        menuId: String
    ) {
        setUiState(ContentUiStates.Loading)

        uploadDishPicture(
            dishId = dishData.id,
            imageByteArray = imageByteArray,
            menuId = menuId,
            onSuccess = {
                getUriOfFileFromFirebaseStorage(
                    menuId = menuId,
                    dishId = dishData.id,
                    onSuccess = { imageUri: Uri ->
                        uploadDishItemData(
                            dishData = dishData.copy(image = imageUri.toString()),
                            menuId = menuId,
                            onSuccess = {
                                setUiState(ContentUiStates.Show)
                                setUiIntent(ContentUiIntents.GoToMenuListScreen)
                            }
                        )
                    }
                )
            }
        )
    }

    private fun uploadDishItemData(dishData: DishData, menuId: String, onSuccess: () -> Unit) {
        uploadDataUseCaseInterface.uploadMenuDishData(
            data = dishData,
            menuId = menuId,
            documentId = dishData.id,
            onSuccess = {
                onSuccess()
            },
            onFailure = {
                setUiState(ContentUiStates.Error)
            }
        )
    }

    private fun getUriOfFileFromFirebaseStorage(
        menuId: String,
        dishId: String,
        onSuccess: (uri: Uri) -> Unit,
    ) {
        downloadFileUseCaseInterface.downloadUriOfDishPicture(
            menuId = menuId,
            dishId = dishId,
            onSuccess = { uri ->
                onSuccess(uri)
            },
            onFailure = {
                setUiState(ContentUiStates.Error)
            }
        )
    }

    private fun uploadDishPicture(
        dishId: String,
        imageByteArray: ByteArray,
        menuId: String,
        onSuccess: () -> Unit,
    ) {
        uploadFileUseCaseInterface.uploadDishPictureUsingByteArray(
            menuId = menuId,
            dishId = dishId,
            byteArray = imageByteArray,
            onSuccess = {
                onSuccess()
            },
            onFailure = {
                setUiState(ContentUiStates.Error)
            }
        )
    }

    private fun generateText(imageBitmap: Bitmap, parameters: List<String?>) {
        viewModelScope.launch {
            generateAiTextUseCaseInterface.generateFunnyNameOfDishUsingImage(
                imageBitmap = imageBitmap,
                parameters = parameters,
                onResponse = { response: String ->
                    Log.d("DAVAI", "AI response: $response")
                    val dishItem: DishData = dishItemData.value.copy(name = response)
                    Log.d("DAVAI", "AI dish item: $dishItem")
                    setDishItemData(dishItem)
                    setUiState(ContentUiStates.Show)
                },
                onFailure = {
                    setUiState(ContentUiStates.Error)
                },
            )
        }
    }

}