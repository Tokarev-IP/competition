package com.example.catalog.content.presentation.viewmodel

import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.viewModelScope
import com.example.catalog.content.domain.data.DishData
import com.example.catalog.content.domain.data.InfoImageData
import com.example.catalog.content.domain.data.MenuInfoData
import com.example.catalog.content.domain.data.SectionData
import com.example.catalog.content.presentation.ContentUiEvents
import com.example.catalog.content.presentation.ContentUiIntents
import com.example.catalog.content.presentation.ContentUiStates
import com.example.catalog.content.presentation.base.ContentBaseViewModel
import com.example.catalog.content.presentation.viewmodel.actions.EditDishItemActionsInterface
import com.example.catalog.content.presentation.viewmodel.actions.EditDishListActionsInterface
import com.example.catalog.content.presentation.viewmodel.actions.EditInfoImageListActionsInterface
import com.example.catalog.content.presentation.viewmodel.actions.EditMenuInfoActionsInterface
import com.example.catalog.content.presentation.viewmodel.actions.EditSectionListActionsInterface
import com.example.catalog.content.presentation.viewmodel.actions.GetDataActionsInterface
import com.example.catalog.content.presentation.viewmodel.actions.MenuActionsInterface
import com.example.catalog.content.presentation.viewmodel.actions.PdfFileActionsInterface
import com.example.catalog.login.domain.interfaces.PhoneAuthUseCaseInterface
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class ContentViewModel @Inject constructor(
    private val phoneAuthUseCaseInterface: PhoneAuthUseCaseInterface,
    private val editDishListActionsInterface: EditDishListActionsInterface,
    private val editDishItemActionsInterface: EditDishItemActionsInterface,
    private val getDataActionsInterface: GetDataActionsInterface,
    private val pdfFileActionsInterface: PdfFileActionsInterface,
    private val menuActionsInterface: MenuActionsInterface,
    private val editSectionListActionsInterface: EditSectionListActionsInterface,
    private val editInfoImageListActionsInterface: EditInfoImageListActionsInterface,
    private val editMenuInfoActionsInterface: EditMenuInfoActionsInterface,
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
                setDishItemData(
                    data = DishData(
                        id = UUID.randomUUID().toString(),
                        sectionId = uiEvent.sectionId,
                    )
                )
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

            is ContentUiEvents.TransformUpdatedDishImage -> {
                transformUpdatedDishImage(
                    imageBitmap = uiEvent.imageBitmap,
                    dishData = getDishItemData()
                )
            }

            is ContentUiEvents.SetUpdatedDishImage -> {
                setUpdatedDishImage(
                    imageUri = uiEvent.imageUri,
                    dishData = getDishItemData()
                )
            }

            is ContentUiEvents.SetDishData -> {
                setDishItemData(
                    getDishItemData().copy(
                        name = uiEvent.name,
                        price = uiEvent.price.toDouble(),
                        weight = uiEvent.weight.toDouble(),
                        description = uiEvent.description
                    )
                )
            }

            is ContentUiEvents.DeleteDishItem -> {
                menuIdVM?.let { id ->
                    deleteDish(
                        menuId = id,
                        dishId = uiEvent.dishData.id,
                        dishList = getDishList(),
                    )
                } ?: setUiIntent(ContentUiIntents.GoToCheckIdScreen)
            }

            is ContentUiEvents.SaveMenuAsPdfFile -> {
                menuIdVM?.let { id ->
                    uiEvent.language?.let { language ->
                        createTranslatedMenuPdfFile(
                            menuId = id,
                            folderUri = uiEvent.folderUri,
                            dishList = getDishList(),
                            translateLanguage = language,
                        )
                    } ?: createMenuPdfFile(
                        menuId = id,
                        folderUri = uiEvent.folderUri,
                        dishList = getDishList(),
                    )
                } ?: setUiIntent(ContentUiIntents.GoToCheckIdScreen)
            }

            is ContentUiEvents.DownloadDishAndSectionLists -> {
                menuIdVM?.let { id ->
                    getDishAndSectionDataList(menuId = id)
                } ?: setUiIntent(ContentUiIntents.GoToCheckIdScreen)
            }

            is ContentUiEvents.CreateSectionItem -> {
                setUiIntent(
                    ContentUiIntents.GoToEditSectionScreen(
                        sectionData = SectionData(
                            id = UUID.randomUUID().toString(),
                        )
                    )
                )
            }

            is ContentUiEvents.SaveSectionItem -> {
                menuIdVM?.let { id ->
                    saveSectionItem(
                        sectionData = uiEvent.sectionData,
                        sectionList = getSectionList(),
                        menuId = id,
                    )
                } ?: setUiIntent(ContentUiIntents.GoToCheckIdScreen)
            }

            is ContentUiEvents.EditSectionItem -> {
                setUiIntent(
                    ContentUiIntents.GoToEditSectionScreen(
                        sectionData = uiEvent.sectionData
                    )
                )
            }

            is ContentUiEvents.DeleteSection -> {
                //todo
            }

            is ContentUiEvents.ShowDishListOfSection -> {
                getDishListOfTheSpecificSection(
                    dishList = getDishList(),
                    sectionData = uiEvent.sectionData
                )
            }

            is ContentUiEvents.SaveInfoImage -> {
                menuIdVM?.let { id ->
                    saveInfoImageItem(
                        menuId = id,
                        uri = uiEvent.imageUri,
                        imageId = UUID.randomUUID().toString(),
                        infoImageList = getInfoImageData(),
                    )
                } ?: setUiIntent(ContentUiIntents.GoToCheckIdScreen)
            }

            is ContentUiEvents.SaveMenuInfo -> {
                menuIdVM?.let { id ->
                    saveMenuInfo(
                        menuId = id,
                        menuInfoData = uiEvent.menuInfoData
                    )
                } ?: setUiIntent(ContentUiIntents.GoToCheckIdScreen)
            }

            is ContentUiEvents.DownloadInfoImageList -> {
                menuIdVM?.let { id ->
                    getInfoImageList(id)
                } ?: setUiIntent(ContentUiIntents.GoToCheckIdScreen)
            }

            is ContentUiEvents.DownloadMenuInfo -> {
                menuIdVM?.let { id ->
                    getMenuInfoData(id)
                } ?: setUiIntent(ContentUiIntents.GoToCheckIdScreen)
            }

            is ContentUiEvents.EditInfoImageList -> {
                menuIdVM?.let { id ->
                    getInfoImageList(id)
                    setUiIntent(ContentUiIntents.GoToEditInfoImageListScreen)
                } ?: setUiIntent(ContentUiIntents.GoToCheckIdScreen)
            }

            is ContentUiEvents.EditMenuInfo -> {
                menuIdVM?.let { id ->
                    getMenuInfoData(id)
                    setUiIntent(ContentUiIntents.GoToEditMenuInfoScreen)
                } ?: setUiIntent(ContentUiIntents.GoToCheckIdScreen)
            }

            is ContentUiEvents.DeleteInfoImage -> {
                menuIdVM?.let { id ->
                    deleteInfoImage(
                        menuId = id,
                        imageId = uiEvent.imageId,
                        infoImageList = getInfoImageData()
                    )
                } ?: setUiIntent(ContentUiIntents.GoToCheckIdScreen)
            }
            
            is ContentUiEvents.ShowMenu -> {
                menuIdVM?.let { id ->
                    getViewMenuData(id)
                    setUiIntent(ContentUiIntents.GoToMenuScreen)
                } ?: setUiIntent(ContentUiIntents.GoToCheckIdScreen)
            }

            is ContentUiEvents.DownloadMenu -> {
                menuIdVM?.let { id ->
                    getViewMenuData(id)
                } ?: setUiIntent(ContentUiIntents.GoToCheckIdScreen)
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
                    setUiIntent(ContentUiIntents.GoToSectionListScreen)
                    getDishAndSectionDataList(menuId)
                },
                onErrorMessage = { errorMessage ->
                    setUiIntent(ContentUiIntents.ShowSnackBarMsg(errorMessage))
                    setUiState(ContentUiStates.Error)
                }
            )
        }
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
    }

    private fun createMenuId(menuId: String, userId: String) {
        setUiState(ContentUiStates.Loading)
        viewModelScope.launch {
            menuActionsInterface.createMenuId(
                menuId = menuId,
                userId = userId,
                onMenuId = { menuId ->
                    menuIdVM = menuId
                    setUiIntent(ContentUiIntents.GoToSectionListScreen)
                    getDishAndSectionDataList(menuId)
                },
                onErrorMessage = { message ->
                    setUiIntent(ContentUiIntents.ShowSnackBarMsg(message))
                    setUiState(ContentUiStates.Error)
                }
            )
        }
    }

    private fun setUpdatedDishImage(
        imageUri: Uri,
        dishData: DishData,
    ) {
        setUiState(ContentUiStates.Loading)
        viewModelScope.launch {
            editDishItemActionsInterface.setUpdatedDishImage(
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
    }

    private fun transformUpdatedDishImage(imageBitmap: Bitmap, dishData: DishData) {
        setUiState(ContentUiStates.Loading)
        viewModelScope.launch {
            editDishItemActionsInterface.transformUpdatedDishImage(
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
    }

    private fun createMenuPdfFile(menuId: String, folderUri: Uri, dishList: List<DishData>) {
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
    }

    private fun createTranslatedMenuPdfFile(
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
        }
    }

    private fun getDishAndSectionDataList(menuId: String) {
        setUiState(ContentUiStates.Loading)
        viewModelScope.launch {
            getDataActionsInterface.getSectionAndDishDataLists(
                menuId = menuId,
                onData = { dishList, sectionList ->
                    setDishList(dishList)
                    setSectionList(sectionList)
                    setUiState(ContentUiStates.Show)
                },
                onErrorMessage = {
                    setUiIntent(ContentUiIntents.ShowSnackBarMsg(it))
                    setUiState(ContentUiStates.Error)
                }
            )
        }
    }

    private fun saveSectionItem(
        sectionData: SectionData,
        sectionList: List<SectionData>,
        menuId: String
    ) {
        setUiState(ContentUiStates.Loading)
        viewModelScope.launch {
            editSectionListActionsInterface.saveSectionItem(
                data = sectionData,
                menuId = menuId,
                documentId = sectionData.id,
                sectionList = sectionList,
                onUpdatedSectionList = { list: List<SectionData> ->
                    setSectionList(list)
                    setUiIntent(ContentUiIntents.ShowSnackBarMsg("The section was saved"))
                    setUiState(ContentUiStates.Show)
                },
                onErrorMessage = { message ->
                    setUiIntent(ContentUiIntents.ShowSnackBarMsg(message))
                    setUiState(ContentUiStates.Show)
                }
            )
        }
    }

    private fun getDishListOfTheSpecificSection(
        dishList: List<DishData>,
        sectionData: SectionData
    ) {
        setUiState(ContentUiStates.Loading)
        viewModelScope.launch {
            getDataActionsInterface.getDishDataOfTheSpecificSection(
                dishDataList = dishList,
                sectionId = sectionData.id,
                onDishList = { dishList ->
                    setDishList(dishList)
                    setUiIntent(ContentUiIntents.GoToDishListScreen(sectionData))
                    setUiState(ContentUiStates.Show)
                }
            )
        }
    }

    private fun saveMenuInfo(menuId: String, menuInfoData: MenuInfoData) {
        setUiState(ContentUiStates.Loading)
        viewModelScope.launch {
            editMenuInfoActionsInterface.saveMenuInfoData(
                menuId = menuId,
                menuInfoData = menuInfoData,
                onSuccess = {
                    setUiIntent(ContentUiIntents.ShowSnackBarMsg("The info was saved"))
                    setUiState(ContentUiStates.Show)
                },
                onErrorMessage = { message ->
                    setUiIntent(ContentUiIntents.ShowSnackBarMsg(message))
                    setUiState(ContentUiStates.Show)
                }
            )
        }
    }

    private fun getMenuInfoData(menuId: String) {
        setUiState(ContentUiStates.Loading)
        viewModelScope.launch {
            getDataActionsInterface.getMenuInfoData(
                menuId = menuId,
                onMenuInfoData = { data ->
                    setMenuInfoData(data)
                    setUiState(ContentUiStates.Show)
                },
                onEmptyMenuInfoData = {
                    setMenuInfoData(MenuInfoData())
                    setUiState(ContentUiStates.Show)
                },
                onErrorMessage = { message ->
                    setUiIntent(ContentUiIntents.ShowSnackBarMsg(message))
                    setUiState(ContentUiStates.Error)
                },
            )
        }
    }

    private fun getInfoImageList(menuId: String) {
        setUiState(ContentUiStates.Loading)
        viewModelScope.launch {
            getDataActionsInterface.getInfoImageDataList(
                menuId = menuId,
                onInfoImageList = { data ->
                    setInfoImageData(data)
                    setUiState(ContentUiStates.Show)
                },
                onErrorMessage = { message ->
                    setUiIntent(ContentUiIntents.ShowSnackBarMsg(message))
                    setUiState(ContentUiStates.Error)
                }
            )
        }
    }

    private fun saveInfoImageItem(
        menuId: String,
        uri: Uri,
        imageId: String,
        infoImageList: List<InfoImageData>,
    ) {
        setUiState(ContentUiStates.Loading)
        viewModelScope.launch {
            editInfoImageListActionsInterface.saveInfoImageItem(
                menuId = menuId,
                imageId = imageId,
                uri = uri,
                infoImageList = infoImageList,
                onInfoImageListUpdated = { updatedList ->
                    setInfoImageData(updatedList)
                    setUiIntent(ContentUiIntents.ShowSnackBarMsg("The image was saved"))
                    setUiState(ContentUiStates.Show)
                },
                onErrorMessage = { message ->
                    setUiIntent(ContentUiIntents.ShowSnackBarMsg(message))
                    setUiState(ContentUiStates.Show)
                }
            )
        }
    }

    private fun deleteInfoImage(
        menuId: String,
        imageId: String,
        infoImageList: List<InfoImageData>,
    ) {
        setUiState(ContentUiStates.Loading)
        viewModelScope.launch {
            editInfoImageListActionsInterface.deleteInfoImageItem(
                menuId = menuId,
                imageId = imageId,
                infoImageList = infoImageList,
                onInfoImageListUpdated = { updatedList ->
                    setInfoImageData(updatedList)
                    setUiIntent(ContentUiIntents.ShowSnackBarMsg("The image was deleted"))
                    setUiState(ContentUiStates.Show)
                },
                onErrorMessage = { message ->
                    setUiIntent(ContentUiIntents.ShowSnackBarMsg(message))
                    setUiState(ContentUiStates.Show)
                }
            )
        }
    }

    private fun getViewMenuData(
        menuId: String
    ){
        setUiState(ContentUiStates.Loading)
        viewModelScope.launch {
            getDataActionsInterface.getViewMenuData(
                menuId = menuId,
                onViewMenuData = { data ->
                    setMenuViewData(data)
                    setUiState(ContentUiStates.Show)
                },
                onErrorMessage = {
                    setUiIntent(ContentUiIntents.ShowSnackBarMsg(it))
                    setUiState(ContentUiStates.Error)
                }
            )
        }
    }

}