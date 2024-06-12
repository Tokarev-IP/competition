package com.example.catalog.content.presentation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarData
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.catalog.content.presentation.screens.CheckMenuScreen
import com.example.catalog.content.presentation.screens.EditDishScreen
import com.example.catalog.content.presentation.screens.EditInfoScreen
import com.example.catalog.content.presentation.screens.MenuListScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContentActivityCompose(
    modifier: Modifier = Modifier,
    contentViewModel: ContentViewModel,
    navController: NavHostController = rememberNavController(),
    startDestination: String = ContentNavigationRoutes.CheckMenuScreen.route,
) {
    val uiIntent by contentViewModel.getUiIntentsFlow().collectAsState(initial = null)
    var snackBarMsg: String? = null

    when (uiIntent) {
        is ContentUiIntents.GoToEditInfoScreen -> {
            navController.navigate(ContentNavigationRoutes.EditInfoScreen.route)
            contentViewModel.clearUiIntents()
        }

        is ContentUiIntents.GoToEditDishScreen -> {
            navController.navigate(ContentNavigationRoutes.EditDishScreen.route)
            contentViewModel.clearUiIntents()
        }

        is ContentUiIntents.GoToMenuListScreen -> {
            navController.navigate(ContentNavigationRoutes.MenuListScreen.route)
            contentViewModel.clearUiIntents()
        }

        is ContentUiIntents.GoBackNavigation -> {
            navController.navigateUp()
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

    Scaffold(
        modifier = modifier.fillMaxSize(),
        snackbarHost = {
            SnackbarHost(snackBarHostState) { snackbarData: SnackbarData ->
                Snackbar(modifier = modifier.padding(horizontal = 24.dp)) {
                    Text(text = snackbarData.visuals.message)
                }
            }
        },
        topBar = {
            TopAppBar(
                title = { Text(text = "Phone number") },
                navigationIcon = {
                    IconButton(onClick = {
                        //todo
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Go back"
                        )
                    }
                }
            )
        },
    ) { innerPadding ->

        NavHost(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding),
            navController = navController,
            startDestination = startDestination
        ) {

            composable(route = ContentNavigationRoutes.EditDishScreen.route) {
                EditDishScreen(
                    contentViewModel = contentViewModel,
                )
            }

            composable(route = ContentNavigationRoutes.EditInfoScreen.route) {
                EditInfoScreen(
                    contentViewModel = contentViewModel,
                )
            }

            composable(route = ContentNavigationRoutes.CheckMenuScreen.route) {
                CheckMenuScreen(
                    contentViewModel = contentViewModel,
                )
            }

            composable(route = ContentNavigationRoutes.MenuListScreen.route) {
                MenuListScreen(
                    contentViewModel = contentViewModel,
                )
            }
        }
    }
}