package com.example.catalog.content.presentation

import android.graphics.Bitmap
import android.net.Uri
import com.example.catalog.content.domain.data.DishData
import com.example.catalog.content.presentation.base.EventInterface
import com.example.catalog.content.presentation.base.IntentInterface
import com.example.catalog.content.presentation.base.StateInterface
import kotlinx.serialization.Serializable

interface ContentUiStates: StateInterface {
    data object Show : ContentUiStates
    data object Loading : ContentUiStates
    data object Error : ContentUiStates
}

sealed interface ContentUiEvents: EventInterface {
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
    class SaveMenuAsPdfFile(val folderUri: Uri) : ContentUiEvents
}

interface ContentUiIntents: IntentInterface {
    data object GoToEditDishScreen : ContentUiIntents
    data object GoBackNavigation : ContentUiIntents
    data object GoToDishListScreen : ContentUiIntents
    data object GoToCreateMenuScreen : ContentUiIntents
    data object GoToSectionListScreen : ContentUiIntents
    data object GoToCheckIdScreen : ContentUiIntents
    class GoToEditSectionScreen(val name: String, val id: String) : ContentUiIntents
    class ShowSnackBarMsg(val msg: String) : ContentUiIntents
}

interface ScreenRoutes {
    @Serializable
    data object EditDishScreen : ScreenRoutes

    @Serializable
    data object DishListScreen : ScreenRoutes

    @Serializable
    data object CreateMenuScreen : ScreenRoutes

    @Serializable
    data object SectionListScreen : ScreenRoutes

    @Serializable
    class EditSectionScreen(val name: String, val id: String) : ScreenRoutes

    @Serializable
    data object CheckIdScreen: ScreenRoutes
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
    TURKISH("Turkish", "tr"),
    NORWEGIAN("Norwegian", "no"),
    SWEDISH("Swedish", "sv"),
    DANISH("Danish", "da"),
    FINNISH("Finnish", "fi"),
    POLISH("Polish", "pl"),
    ROMANIAN("Romanian", "ro"),
    CZECH("Czech", "cs"),
}