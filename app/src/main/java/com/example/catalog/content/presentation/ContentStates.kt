package com.example.catalog.content.presentation

import android.graphics.Bitmap
import android.net.Uri
import com.example.catalog.content.domain.data.DishData
import kotlinx.serialization.Serializable

interface ContentUiStates {
    data object Show : ContentUiStates
    data object Loading : ContentUiStates
    data object Error : ContentUiStates
}

sealed interface ContentUiEvents {
    class SaveDishItem(val dishData: DishData) : ContentUiEvents
    class EditDishItem(val dishData: DishData) : ContentUiEvents
    data object CreateDishItem : ContentUiEvents
    data object GoBack : ContentUiEvents
    data object DownloadMenuList : ContentUiEvents
    data object CreateMenu : ContentUiEvents
    data object CheckMenuId : ContentUiEvents
    class GenerateDescriptionOfDish(val imageBitmap: Bitmap, val dishName: String) : ContentUiEvents
    class SetInitialImage(val imageUri: Uri) : ContentUiEvents
    class CreateUpdatedImage(val imageBitmap: Bitmap) : ContentUiEvents
    class SaveMenuAsDocFile(val folderUri: Uri, val language: String? = null) : ContentUiEvents
    class SetNamePriceWeightDescription(
        val name: String,
        val price: String,
        val description: String,
        val weight: String,
    ) : ContentUiEvents
    class DeleteDish(val dishData: DishData) : ContentUiEvents
}

interface ContentUiIntents {
    data object GoToEditDishScreen : ContentUiIntents
    data object GoBackNavigation : ContentUiIntents
    data object GoToMenuListScreen : ContentUiIntents
    data object GoToCreateMenuScreen : ContentUiIntents
    class ShowSnackBarMsg(val msg: String) : ContentUiIntents
}

interface ScreenRoutes {
    @Serializable
    data object EditDishScreen : ScreenRoutes

    @Serializable
    data object MenuListScreen : ScreenRoutes

    @Serializable
    data object CreateMenuScreen : ScreenRoutes
}

enum class LanguageList(
    val language: String,
    val code: String,
) {
    ENGLISH("English", "en"),
    GERMAN("German", "de"),
    ITALIAN("Italian", "it"),
    RUSSIAN("Russian", "ru"),
    SPANISH("Spanish", "es"),
    FRENCH("French", "fr"),
    PORTUGUESE("Portuguese", "pt"),
    JAPANESE("Japanese", "ja"),
    KOREAN("Korean", "ko"),
    CHINESE("Chinese", "zh"),
}