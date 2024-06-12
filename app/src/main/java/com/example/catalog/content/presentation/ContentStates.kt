package com.example.catalog.content.presentation

import android.net.Uri
import com.example.catalog.content.domain.data.DishData

interface ContentUiStates {
    data object Show : ContentUiStates
    data object Loading : ContentUiStates
    data object Error : ContentUiStates
}

sealed interface ContentUiEvents {
    class SaveDishItem(val dishData: DishData, val imageUri: Uri?) : ContentUiEvents
    class EditDishItem(val dishData: DishData) : ContentUiEvents
    data object CreateDishItem : ContentUiEvents
    data object GoBack : ContentUiEvents
    data object DownloadMenuList : ContentUiEvents
    data object CreateMenu : ContentUiEvents
    data object CheckMenuId : ContentUiEvents
    class GenerateTextUsingGemini(val uri: Uri, val parameters: List<String?>) : ContentUiEvents
}

interface ContentUiIntents {
    data object GoToEditDishScreen : ContentUiIntents
    data object GoToEditInfoScreen : ContentUiIntents
    data object GoBackNavigation : ContentUiIntents
    data object GoToMenuListScreen : ContentUiIntents
    class ShowSnackBarMsg(val msg: String) : ContentUiIntents
}

enum class ContentNavigationRoutes(val route: String) {
    EditDishScreen("EditDishScreen"),
    EditInfoScreen("EditInfoScreen"),
    MenuListScreen("MenuListScreen"),
    CheckMenuScreen("CheckMenuScreen"),

}

enum class ParametersForGeneratingAiDishName(
    val parameters: List<String>,
    val description: String,
    val responseText: String,
) {
    Taste(
        listOf("Nothing", "Sweet", "Salty", "Sour", "Bitter", "Spicy"),
        description = "Choose taste of dish, which you want to be used in the dish name or leave neutral",
        responseText = "the name should include main taste of the dish - ",
    ),
    Mood(
        listOf(
            "Nothing",
            "Funny",
            "Cute",
            "Sad",
            "Angry",
            "Happy",
            "Interesting",
            "Boring",
            "Serious"
        ),
        description = "Choose mood of dish, which you want to be used in the dish name or leave neutral",
        responseText = "the name should include this mood - ",
    ),
    Season(
        listOf("Nothing", "Winter", "Spring", "Summer", "Autumn"),
        description = "Choose season of dish, which you want to be used in the dish name or leave neutral",
        responseText = "the name should include the season of the dish - ",
    ),
}