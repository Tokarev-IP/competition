package com.example.catalog.login.presentation

import com.example.catalog.content.presentation.base.BaseViewModel
import com.example.catalog.login.data.PhoneAuthProviderCallback
import com.example.catalog.login.domain.interfaces.PhoneAuthUseCaseInterface
import com.google.firebase.FirebaseException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val phoneAuthUseCaseInterface: PhoneAuthUseCaseInterface,
) : BaseViewModel<LoginUiStates, LoginUiIntents, LoginUiEvents>(LoginUiStates.Show) {

    override fun setUiEvent(uiEvent: LoginUiEvents) {
        when (uiEvent) {
            is LoginUiEvents.LoginUsingSmsCode -> {
                setUiState(LoginUiStates.Loading)
                phoneAuthUseCaseInterface.receiveCredential(
                    verificationId = uiEvent.verificationId,
                    code = uiEvent.code,
                    onCredential = { credential: PhoneAuthCredential ->
                        loginWithCredential(credential)
                    }
                )
            }

            is LoginUiEvents.LoginWithGoogle -> {
                setUiState(LoginUiStates.Loading)
                //todo login with google
                setUiState(LoginUiStates.Show)
            }

            is LoginUiEvents.SendLoginSmsCode -> {
                sendLoginSmsCode(uiEvent.phoneAuthOptions)
            }

            is LoginUiEvents.GoBackNavigation -> {
                setUiIntent(LoginUiIntents.GoBackNavigation)
            }

            is LoginUiEvents.LoginUsingPhoneNumber -> {
                setUiIntent(LoginUiIntents.GoToPhoneLoginScreen)
            }

            is LoginUiEvents.GoToLoginChooseScreen -> {
                setUiIntent(LoginUiIntents.GoToLoginChooseScreen)
            }

            is LoginUiEvents.ToWriteDownSmsCode -> {
                setUiIntent(
                    LoginUiIntents.GoToPhoneCodeScreen(
                        verificationId = uiEvent.verificationId,
                    )
                )
                setUiState(LoginUiStates.Show)
            }

            is LoginUiEvents.CheckUser -> {
                getCurrentUserId()
            }
        }
    }

    private fun sendLoginSmsCode(phoneAuthOptions: PhoneAuthOptions) {
        setUiState(LoginUiStates.Loading)
        phoneAuthUseCaseInterface.sendSignInSmsCode(phoneAuthOptions)
    }

    private fun loginWithCredential(credential: PhoneAuthCredential) {
        setUiState(LoginUiStates.Loading)
        phoneAuthUseCaseInterface.signInUsingCredential(
            authCredential = credential,
            onSuccessful = {
                setUiIntent(LoginUiIntents.GoToContentActivity)
                setUiState(LoginUiStates.Show)
            },
            onCanceled = { error: String ->
                setUiState(LoginUiStates.Show)
                setUiIntent(LoginUiIntents.ShowSnackBarMessage(msg = error))
            }
        )
    }

    private fun getCurrentUserId() {
        setUiState(LoginUiStates.Loading)
        phoneAuthUseCaseInterface.getCurrentUser(
            onUserExists = {
                setUiIntent(LoginUiIntents.GoToContentActivity)
                setUiState(LoginUiStates.Show)
            },
            onUserDoesNotExist = {
                setUiState(LoginUiStates.Show)
            }
        )
    }

    internal fun getPhoneAuthProviderCallback(): PhoneAuthProviderCallback {
        return PhoneAuthProviderCallback(
            onCompleted = { phoneAuthCredential: PhoneAuthCredential ->
                loginWithCredential(phoneAuthCredential)
            },
            onFailed = { firebaseException: FirebaseException ->
                setUiIntent(LoginUiIntents.ShowSnackBarMessage(msg = firebaseException.message.toString()))
                setUiState(LoginUiStates.Show)
            },
            onCodeWasSent = { verificationId: String, token: PhoneAuthProvider.ForceResendingToken ->
                setUiIntent(
                    LoginUiIntents.GoToPhoneCodeScreen(
                        verificationId = verificationId,
                    )
                )
                setUiState(LoginUiStates.Show)
            },
            onCodeTimeOut = { phoneNumber: String ->
                setUiIntent(LoginUiIntents.ShowSnackBarMessage(msg = "Code timeout for $phoneNumber"))
                setUiState(LoginUiStates.Show)
            },
        )
    }
}