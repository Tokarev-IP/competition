package com.example.catalog.login.data.interfaces

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions

interface AuthRepositoryInterface {

    fun getCurrentUser(): FirebaseUser?

    fun signOut()

    fun signInWithCredential(
        authCredential: AuthCredential,
        onResult: (task: Task<AuthResult>) -> Unit,
    )

    fun sendSignInCodeToPhoneNumber(phoneAuthOptions: PhoneAuthOptions)

    fun getCredential(verificationId: String, code: String): PhoneAuthCredential
}