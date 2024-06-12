package com.example.catalog.login.presentation.screens.phonecode

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.catalog.login.presentation.LoginViewModel

@Composable
internal fun PhoneCodeScreen(
    loginViewModel: LoginViewModel,
    verificationId: String,
) {
    val uiState by loginViewModel.getUiStatesFlow().collectAsState()

    PhoneCodeView(
        eventHandler = { loginUiEvent ->
            loginViewModel.setUiEvent(loginUiEvent)
        },
        uiState = uiState,
        verificationId = verificationId,
    )
}