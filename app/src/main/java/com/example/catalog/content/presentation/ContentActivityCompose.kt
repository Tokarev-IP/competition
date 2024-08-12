package com.example.catalog.content.presentation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.catalog.content.presentation.screens.CreateMenuScreen
import com.example.catalog.content.presentation.screens.EditDishScreen
import com.example.catalog.content.presentation.screens.MenuListScreen

@Composable
fun ContentActivityCompose(
    modifier: Modifier = Modifier,
    contentViewModel: ContentViewModel,
    navController: NavHostController = rememberNavController(),
) {
    val uiIntent by contentViewModel.getUiIntentsFlow().collectAsState(initial = null)
    val snackBarHostState = remember { SnackbarHostState() }

    when (uiIntent) {
        is ContentUiIntents.GoToEditDishScreen -> {
            navController.navigate(ScreenRoutes.EditDishScreen)
            contentViewModel.clearUiIntents()
        }

        is ContentUiIntents.GoToMenuListScreen -> {
            navController.navigate(ScreenRoutes.MenuListScreen) {
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
                popUpTo(ScreenRoutes.MenuListScreen) { inclusive = true }
            }
            contentViewModel.clearUiIntents()
        }

        is ContentUiIntents.ShowSnackBarMsg -> {
            val snackBarMsg = (uiIntent as ContentUiIntents.ShowSnackBarMsg).msg
            LaunchedEffect(key1 = snackBarMsg) {
                snackBarHostState.showSnackbar(
                    message = snackBarMsg,
                    actionLabel = "Result of action",
                    duration = SnackbarDuration.Short
                )
            }
        }
    }

    Box {
        NavHost(
            modifier = modifier
                .fillMaxSize(),
            navController = navController,
            startDestination = ScreenRoutes.CreateMenuScreen,
            enterTransition = { fadeIn(tween(300)) },
            exitTransition = { fadeOut(tween(300)) },
        ) {
            composable<ScreenRoutes.EditDishScreen> {
                EditDishScreen(contentViewModel = contentViewModel)
            }

            composable<ScreenRoutes.CreateMenuScreen> {
                CreateMenuScreen(contentViewModel = contentViewModel)
            }

            composable<ScreenRoutes.MenuListScreen> {
                MenuListScreen(contentViewModel = contentViewModel)
            }
        }
    }
}