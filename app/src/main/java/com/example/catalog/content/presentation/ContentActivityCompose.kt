package com.example.catalog.content.presentation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.AnimationSpec
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
import androidx.navigation.toRoute
import com.example.catalog.content.presentation.screens.CheckMenuScreen
import com.example.catalog.content.presentation.screens.DishImageChoiceScreen
import com.example.catalog.content.presentation.screens.EditDishScreen
import com.example.catalog.content.presentation.screens.EditInfoScreen
import com.example.catalog.content.presentation.screens.MenuListScreen

@Composable
fun ContentActivityCompose(
    modifier: Modifier = Modifier,
    contentViewModel: ContentViewModel,
    navController: NavHostController = rememberNavController(),
) {
    val uiIntent by contentViewModel.getUiIntentsFlow().collectAsState(initial = null)
    var snackBarMsg: String? = null

    when (uiIntent) {
        is ContentUiIntents.GoToEditInfoScreen -> {
            navController.navigate(ScreenRoutes.EditInfoScreen)
            contentViewModel.clearUiIntents()
        }

        is ContentUiIntents.GoToEditDishScreen -> {
            navController.navigate(ScreenRoutes.EditDishScreen)
            contentViewModel.clearUiIntents()
        }

        is ContentUiIntents.GoToMenuListScreen -> {
            navController.navigate(ScreenRoutes.MenuListScreen)
            contentViewModel.clearUiIntents()
        }

        is ContentUiIntents.GoBackNavigation -> {
            navController.navigateUp()
            contentViewModel.clearUiIntents()
        }

        is ContentUiIntents.GoToUpdateDishImageScreen -> {
            val imageUriString =
                (uiIntent as ContentUiIntents.GoToUpdateDishImageScreen).imageUriString
            navController.navigate(
                ScreenRoutes.UpdateDishImageScreen(imageUriString)
            )
            contentViewModel.clearUiIntents()
        }
    }

    val snackBarHostState = remember { SnackbarHostState() }

    LaunchedEffect(key1 = snackBarMsg) {
        snackBarMsg?.let { msg ->
            snackBarHostState.showSnackbar(
                message = msg,
                actionLabel = "Result of action",
                duration = SnackbarDuration.Short
            )
        }
    }

//    Scaffold(
//        modifier = modifier.fillMaxSize(),
//        snackbarHost = {
//            SnackbarHost(snackBarHostState) { snackbarData: SnackbarData ->
//                Snackbar(modifier = modifier.padding(horizontal = 24.dp)) {
//                    Text(text = snackbarData.visuals.message)
//                }
//            }
//        },
//        topBar = {
//            TopAppBar(
//                title = {  },
//                navigationIcon = {
//                    IconButton(onClick = {
//                        contentViewModel.setUiEvent(ContentUiEvents.GoBack)
//                    }) {
//                        Icon(
//                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
//                            contentDescription = "Go back"
//                        )
//                    }
//                }
//            )
//        },
//    ) { innerPadding ->

    Box {
        NavHost(
            modifier = modifier
                .fillMaxSize(),
            navController = navController,
            startDestination = ScreenRoutes.CheckMenuScreen,
            enterTransition = { fadeIn(tween(200)) },
            exitTransition = { fadeOut(tween(200)) },
        ) {
            composable<ScreenRoutes.EditDishScreen> {
                EditDishScreen(
                    contentViewModel = contentViewModel,
                )
            }

            composable<ScreenRoutes.EditInfoScreen> {
                EditInfoScreen(
                    contentViewModel = contentViewModel,
                )
            }

            composable<ScreenRoutes.CheckMenuScreen> {
                CheckMenuScreen(
                    contentViewModel = contentViewModel,
                )
            }

            composable<ScreenRoutes.MenuListScreen> {
                MenuListScreen(
                    contentViewModel = contentViewModel,
                )
            }

            composable<ScreenRoutes.UpdateDishImageScreen> { backStackEntry ->
                val updateDishImage: ScreenRoutes.UpdateDishImageScreen =
                    backStackEntry.toRoute()

                DishImageChoiceScreen(
                    contentViewModel = contentViewModel,
                    imageUriString = updateDishImage.imageUriString,
                )
            }

        }
    }
}