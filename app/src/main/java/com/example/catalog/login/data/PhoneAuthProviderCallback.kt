package com.example.catalog.login.data

import com.google.firebase.FirebaseException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider

class PhoneAuthProviderCallback(
    private val onCompleted: (phoneAuthCredential: PhoneAuthCredential) -> Unit,
    private val onFailed: (firebaseException: FirebaseException) -> Unit,
    private val onCodeWasSent: (verificationId: String, PhoneAuthProvider.ForceResendingToken) -> Unit,
    private val onCodeTimeOut: (phoneNumber: String) -> Unit,
): PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

    override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {
        onCompleted(phoneAuthCredential)
    }

    override fun onVerificationFailed(firebaseException: FirebaseException) {
        onFailed(firebaseException)
    }

    override fun onCodeSent(verificationId: String, authToken: PhoneAuthProvider.ForceResendingToken) {
        onCodeWasSent(verificationId, authToken)
    }

    override fun onCodeAutoRetrievalTimeOut(phoneNumber: String) {
        onCodeTimeOut(phoneNumber)
    }
}