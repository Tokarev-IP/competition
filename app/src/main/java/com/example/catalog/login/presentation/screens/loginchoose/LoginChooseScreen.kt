package com.example.catalog.login.presentation.screens.loginchoose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.catalog.login.presentation.LoginUiEvents
import com.example.catalog.login.presentation.LoginViewModel

@Composable
internal fun LoginChooseScreen(
    loginViewModel: LoginViewModel,
) {
    val uiState by loginViewModel.getUiStatesFlow().collectAsState()

    LaunchedEffect(key1 = Unit) {
        loginViewModel.setUiEvent(LoginUiEvents.CheckUser)
    }

    LoginChooseView(
        uiState = uiState,
        eventHandler = { loginUiEvent ->
            loginViewModel.setUiEvent(loginUiEvent)
        }
    )
}