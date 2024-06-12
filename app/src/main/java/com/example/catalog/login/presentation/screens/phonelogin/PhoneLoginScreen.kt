package com.example.catalog.login.presentation.screens.phonelogin

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.catalog.login.presentation.LoginViewModel

@Composable
internal fun PhoneLoginScreen(
    loginViewModel: LoginViewModel,
    onPhoneLogin: (phoneNumber: String) -> Unit,
) {
    val uiState by loginViewModel.getUiStatesFlow().collectAsState()

    PhoneLoginView(
        onPhoneLogin = { phoneNumber ->
            onPhoneLogin(phoneNumber)
        },
        eventHandler = { loginUiEvent ->
            loginViewModel.setUiEvent(loginUiEvent)
        },
        uiState = uiState,
    )
}