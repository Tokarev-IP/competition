package com.example.catalog.login.presentation.screens.loginchoose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.catalog.login.presentation.LoginViewModel

@Composable
internal fun LoginChooseScreen(
    loginViewModel: LoginViewModel,
) {
    val uiState by loginViewModel.getUiStatesFlow().collectAsState()

    LoginChooseView(
        uiState = uiState,
        eventHandler = { loginUiEvent ->
            loginViewModel.setUiEvent(loginUiEvent)
        }
    )
}