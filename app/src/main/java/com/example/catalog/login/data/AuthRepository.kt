package com.example.catalog.login.data

import com.example.catalog.login.data.interfaces.AuthRepositoryInterface
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(): AuthRepositoryInterface {

    private val auth = FirebaseAuth.getInstance()

    override fun getCurrentUser(): FirebaseUser? = auth.currentUser

    override fun signOut(): Unit = auth.signOut()

    override fun signInWithCredential(
        authCredential: AuthCredential,
        onResult: (task: Task<AuthResult>) -> Unit,
    ) {
        auth.signInWithCredential(authCredential).addOnCompleteListener { task: Task<AuthResult> ->
            onResult(task)
        }
    }

    override fun sendSignInCodeToPhoneNumber(phoneAuthOptions: PhoneAuthOptions) {
        PhoneAuthProvider.verifyPhoneNumber(phoneAuthOptions)
    }

    override fun getCredential(
        verificationId: String,
        code: String,
    ) = PhoneAuthProvider.getCredential(verificationId, code)
}