package com.example.catalog.content.presentation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.catalog.content.domain.data.SectionData
import com.example.catalog.content.presentation.screens.CheckIdScreen
import com.example.catalog.content.presentation.screens.CreateMenuScreen
import com.example.catalog.content.presentation.screens.EditDishScreen
import com.example.catalog.content.presentation.screens.DishListScreen
import com.example.catalog.content.presentation.screens.EditSectionScreen
import com.example.catalog.content.presentation.screens.SectionListScreen
import com.example.catalog.content.presentation.viewmodel.ContentViewModel

@Composable
fun ContentActivityCompose(
    modifier: Modifier = Modifier,
    contentViewModel: ContentViewModel,
    navController: NavHostController = rememberNavController(),
) {
    val uiIntent by contentViewModel.getUiIntentsFlow().collectAsState(initial = null)

    when (uiIntent) {
        is ContentUiIntents.GoToEditDishScreen -> {
            navController.navigate(ScreenRoutes.EditDishScreen)
            contentViewModel.clearUiIntents()
        }

        is ContentUiIntents.GoToDishListScreen -> {
            val sectionData = (uiIntent as ContentUiIntents.GoToDishListScreen).sectionData
            navController.navigate(
                ScreenRoutes.DishListScreen(
                    id = sectionData.id,
                    name = sectionData.name,
                    position = sectionData.position,
                )
            ) {
                popUpTo(ScreenRoutes.CreateMenuScreen) { inclusive = true }
            }
            contentViewModel.clearUiIntents()
        }

        is ContentUiIntents.GoBackNavigation -> {
            navController.navigateUp()
            contentViewModel.clearUiIntents()
        }

        is ContentUiIntents.GoToCreateMenuScreen -> {
            navController.navigate(ScreenRoutes.CreateMenuScreen) {
                popUpTo(ScreenRoutes.DishListScreen) { inclusive = true }
            }
            contentViewModel.clearUiIntents()
        }

        is ContentUiIntents.GoToSectionListScreen -> {
            navController.navigate(ScreenRoutes.SectionListScreen)
            contentViewModel.clearUiIntents()
        }

        is ContentUiIntents.GoToEditSectionScreen -> {
            val sectionData = (uiIntent as ContentUiIntents.GoToEditSectionScreen).sectionData
            navController.navigate(
                ScreenRoutes.EditSectionScreen(
                    id = sectionData.id,
                    name = sectionData.name,
                    position = sectionData.position,
                )
            )
            contentViewModel.clearUiIntents()
        }

        is ContentUiIntents.GoToCheckIdScreen -> {
            navController.navigate(ScreenRoutes.CheckIdScreen)
            contentViewModel.clearUiIntents()
        }
    }

    Box {
        NavHost(
            modifier = modifier
                .fillMaxSize(),
            navController = navController,
            startDestination = ScreenRoutes.CheckIdScreen,
            enterTransition = { fadeIn(tween(300)) },
            exitTransition = { fadeOut(tween(300)) },
        ) {
            composable<ScreenRoutes.EditDishScreen> {
                EditDishScreen(contentViewModel = contentViewModel)
            }

            composable<ScreenRoutes.CreateMenuScreen> {
                CreateMenuScreen(contentViewModel = contentViewModel)
            }

            composable<ScreenRoutes.DishListScreen> { backStackEntry ->
                val data = backStackEntry.toRoute<ScreenRoutes.DishListScreen>()
                val sectionData = SectionData(
                    id = data.id,
                    name = data.name,
                    position = data.position,
                )
                DishListScreen(
                    contentViewModel = contentViewModel,
                    sectionData = sectionData,
                )
            }

            composable<ScreenRoutes.SectionListScreen> {
                SectionListScreen(contentViewModel = contentViewModel)
            }

            composable<ScreenRoutes.EditSectionScreen> { backStackEntry ->
                val data = backStackEntry.toRoute<ScreenRoutes.EditSectionScreen>()
                val sectionData = SectionData(
                    id = data.id,
                    name = data.name,
                    position = data.position,
                )
                EditSectionScreen(
                    contentViewModel = contentViewModel,
                    sectionData = sectionData,
                )
            }

            composable<ScreenRoutes.CheckIdScreen> {
                CheckIdScreen(contentViewModel = contentViewModel)
            }
        }
    }
}