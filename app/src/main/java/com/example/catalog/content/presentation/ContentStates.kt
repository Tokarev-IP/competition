package com.example.catalog.content.presentation

import android.graphics.Bitmap
import android.net.Uri
import com.example.catalog.content.domain.data.DishData
import com.example.catalog.content.domain.data.MenuInfoData
import com.example.catalog.content.domain.data.SectionData
import kotlinx.serialization.Serializable

interface ContentUiStates {
    data object Show : ContentUiStates
    data object Loading : ContentUiStates
    data object Error : ContentUiStates
}

sealed interface ContentUiEvents {
    class CreateDishItem(val sectionId: String) : ContentUiEvents
    class SaveDishItem(val dishData: DishData) : ContentUiEvents
    class EditDishItem(val dishData: DishData) : ContentUiEvents
    class DeleteDishItem(val dishData: DishData) : ContentUiEvents
    class ShowDishListOfSection(val sectionData: SectionData) : ContentUiEvents

    data object CreateSectionItem : ContentUiEvents
    class SaveSectionItem(val sectionData: SectionData) : ContentUiEvents
    class EditSectionItem(val sectionData: SectionData) : ContentUiEvents
    class DeleteSection(val sectionData: SectionData) : ContentUiEvents

    data object GoBack : ContentUiEvents
    data object DownloadMenuList : ContentUiEvents
    data object CreateMenu : ContentUiEvents
    data object CheckMenuId : ContentUiEvents
    data object DownloadDishAndSectionLists : ContentUiEvents
    data object EditInfoImageList: ContentUiEvents
    data object EditMenuInfo: ContentUiEvents
    data object DownloadInfoImageList : ContentUiEvents
    data object DownloadMenuInfo : ContentUiEvents
    data object ShowMenu: ContentUiEvents
    data object DownloadMenu: ContentUiEvents

    class GenerateDescriptionOfDish(val imageBitmap: Bitmap, val dishName: String) : ContentUiEvents
    class SetUpdatedDishImage(val imageUri: Uri) : ContentUiEvents
    class TransformUpdatedDishImage(val imageBitmap: Bitmap) : ContentUiEvents
    class SetDishData(
        val name: String,
        val price: String,
        val description: String,
        val weight: String,
    ) : ContentUiEvents

    class SaveMenuAsPdfFile(val folderUri: Uri, val language: String? = null) : ContentUiEvents
    class SaveInfoImage(val imageUri: Uri) : ContentUiEvents
    class SaveMenuInfo(val menuInfoData: MenuInfoData): ContentUiEvents
    class DeleteInfoImage(val imageId: String): ContentUiEvents
}

interface ContentUiIntents {
    data object GoToEditDishScreen : ContentUiIntents
    data object GoBackNavigation : ContentUiIntents
    class GoToDishListScreen(val sectionData: SectionData) : ContentUiIntents
    data object GoToCreateMenuScreen : ContentUiIntents
    data object GoToSectionListScreen : ContentUiIntents
    data object GoToCheckIdScreen : ContentUiIntents
    data object GoToEditInfoImageListScreen : ContentUiIntents
    data object GoToEditMenuInfoScreen : ContentUiIntents
    data object GoToMenuScreen: ContentUiIntents
    class GoToEditSectionScreen(val sectionData: SectionData) : ContentUiIntents
    class ShowSnackBarMsg(val msg: String) : ContentUiIntents
}

interface ScreenRoutes {
    @Serializable
    data object EditDishScreen : ScreenRoutes

    @Serializable
    class DishListScreen(val id: String, val name: String, val position: Int) : ScreenRoutes

    @Serializable
    data object CreateMenuScreen : ScreenRoutes

    @Serializable
    data object SectionListScreen : ScreenRoutes

    @Serializable
    class EditSectionScreen(val id: String, val name: String, val position: Int) : ScreenRoutes

    @Serializable
    data object CheckIdScreen : ScreenRoutes

    @Serializable

    data object EditInfoImageListScreen : ScreenRoutes

    @Serializable
    data object EditMenuInfoScreen : ScreenRoutes

    @Serializable
    data object MenuScreen: ScreenRoutes
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