package com.example.catalog.login.domain.usecases

import com.example.catalog.login.data.interfaces.AuthRepositoryInterface
import com.example.catalog.login.domain.interfaces.PhoneAuthUseCaseInterface
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PhoneAuthUseCase @Inject constructor(
    private val authRepositoryInterface: AuthRepositoryInterface,
) : PhoneAuthUseCaseInterface {

    override fun getCurrentUser(
        onUserExists: (firebaseUser: FirebaseUser) -> Unit,
        onUserDoesNotExist: (msg: String) -> Unit
    ) {
        val user = authRepositoryInterface.getCurrentUser()
        if (user != null) {
            onUserExists(user)
        } else {
            onUserDoesNotExist("User does not exist")
        }
    }

    override fun signInUsingCredential(
        authCredential: AuthCredential,
        onSuccessful: () -> Unit,
        onCanceled: (e: String) -> Unit,
    ) {
        authRepositoryInterface.signInWithCredential(
            authCredential = authCredential,
            onResult = { task: Task<AuthResult> ->
                if (task.isSuccessful) {
                    onSuccessful()
                }
                task.exception?.let { e: Exception ->
                    when (e) {
                        is FirebaseAuthInvalidCredentialsException -> {
                            onCanceled(e.message.toString())
                        }

                        is FirebaseNetworkException -> {
                            onCanceled(e.message.toString())
                        }

                        is FirebaseAuthException -> {
                            onCanceled(e.message.toString())
                        }

                        is FirebaseTooManyRequestsException -> {
                            onCanceled(e.message.toString())
                        }
                    }
                }
            }
        )
    }

    override fun sendSignInSmsCode(phoneAuthOptions: PhoneAuthOptions) {
        authRepositoryInterface.sendSignInCodeToPhoneNumber(phoneAuthOptions)
    }


    override fun receiveCredential(
        verificationId: String,
        code: String,
        onCredential: (credential: PhoneAuthCredential) -> Unit,
    ) {
        val credential = authRepositoryInterface.getCredential(verificationId, code)
        onCredential(credential)
    }
}