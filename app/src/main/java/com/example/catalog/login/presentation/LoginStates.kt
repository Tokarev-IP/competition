package com.example.catalog.login.presentation

import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider

interface LoginUiStates {
    object Show : LoginUiStates
    object Loading : LoginUiStates
    object Error : LoginUiStates
}

sealed interface LoginUiEvents {
    data object GoBackNavigation : LoginUiEvents
    data object LoginUsingPhoneNumber : LoginUiEvents
    data object GoToLoginChooseScreen : LoginUiEvents
    class SendLoginSmsCode(val phoneAuthOptions: PhoneAuthOptions) : LoginUiEvents
    data object LoginWithGoogle : LoginUiEvents
    class LoginUsingSmsCode(
        val code: String,
        val verificationId: String,
    ) : LoginUiEvents

    class ToWriteDownSmsCode(
        val verificationId: String,
        val token: PhoneAuthProvider.ForceResendingToken,
    ) : LoginUiEvents

    data object CheckUser : LoginUiEvents
}

interface LoginUiIntents {
    object GoToPhoneLoginScreen : LoginUiIntents
    object GoBackNavigation : LoginUiIntents
    object GoToContentActivity : LoginUiIntents
    object GoToLoginChooseScreen : LoginUiIntents
    class GoToPhoneCodeScreen(val verificationId: String) : LoginUiIntents
    class ShowSnackBarMessage(val msg: String) : LoginUiIntents
}

enum class LoginNavigationRoutes(val route: String) {
    LoginChooseScreen("LoginChooseScreen"),
    PhoneLoginScreen("PhoneLoginScreen"),
    PhoneCodeScreen("PhoneCodeScreen"),
}