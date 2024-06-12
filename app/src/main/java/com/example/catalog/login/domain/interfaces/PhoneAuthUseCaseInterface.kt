package com.example.catalog.login.domain.interfaces

import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions

interface PhoneAuthUseCaseInterface {

    fun getCurrentUser(
        onUserExists: (firebaseUser: FirebaseUser) -> Unit,
        onUserDoesNotExist: (msg: String) -> Unit
    )

    fun signInUsingCredential(
        authCredential: AuthCredential,
        onSuccessful: () -> Unit,
        onCanceled: (e: String) -> Unit,
    )

    fun sendSignInSmsCode(phoneAuthOptions: PhoneAuthOptions)

    fun receiveCredential(
        verificationId: String,
        code: String,
        onCredential: (credential: PhoneAuthCredential) -> Unit,
    )
}