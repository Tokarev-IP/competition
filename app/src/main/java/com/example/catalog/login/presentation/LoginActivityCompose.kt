package com.example.catalog.login.presentation

import android.util.Log
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
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.catalog.login.presentation.screens.loginchoose.LoginChooseScreen
import com.example.catalog.login.presentation.screens.phonecode.PhoneCodeScreen
import com.example.catalog.login.presentation.screens.phonelogin.PhoneLoginScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun LoginActivityCompose(
    modifier: Modifier = Modifier,
    loginViewModel: LoginViewModel,
    navController: NavHostController = rememberNavController(),
    startDestination: String = LoginNavigationRoutes.LoginChooseScreen.route,
    onPhoneLogin: (phoneNumber: String) -> Unit
) {
    val uiIntent by loginViewModel.getUiIntentsFlow().collectAsState(initial = null)
    var snackBarMsg: String? = null

    when (uiIntent) {
        is LoginUiIntents.GoBackNavigation -> {
            navController.navigateUp()
            loginViewModel.clearUiIntents()
        }

        is LoginUiIntents.GoToPhoneLoginScreen -> {
            navController.navigate(LoginNavigationRoutes.PhoneLoginScreen.route)
            loginViewModel.clearUiIntents()
        }

        is LoginUiIntents.ShowSnackBarMessage -> {
            snackBarMsg = (uiIntent as LoginUiIntents.ShowSnackBarMessage).msg
            loginViewModel.clearUiIntents()
        }

        is LoginUiIntents.GoToPhoneCodeScreen -> {
            val data = (uiIntent as LoginUiIntents.GoToPhoneCodeScreen).verificationId
            navController.navigate(LoginNavigationRoutes.PhoneCodeScreen.route + "/${data}")
            loginViewModel.clearUiIntents()
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
                    IconButton(onClick = { loginViewModel.setUiEvent(LoginUiEvents.GoBackNavigation) }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Go back"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->

        NavHost(
            modifier = modifier
                .padding(innerPadding)
                .fillMaxSize(),
            navController = navController,
            startDestination = startDestination
        ) {
            composable(route = LoginNavigationRoutes.LoginChooseScreen.route) {
                LoginChooseScreen(loginViewModel = loginViewModel)
            }

            composable(route = LoginNavigationRoutes.PhoneLoginScreen.route) {
                PhoneLoginScreen(
                    onPhoneLogin = { phoneNumber: String ->
                        onPhoneLogin(phoneNumber)
                    },
                    loginViewModel = loginViewModel,
                )
            }

            composable(
                route = LoginNavigationRoutes.PhoneCodeScreen.route + "/{verificationId}",
                arguments = listOf(
                    navArgument("verificationId") { type = NavType.StringType },
                ),
            ) { backStackEntry ->
                val verificationIdString =
                    backStackEntry.arguments?.getString("verificationId") ?: ""

                PhoneCodeScreen(
                    loginViewModel = loginViewModel,
                    verificationId = verificationIdString,
                )
            }
        }
    }
}