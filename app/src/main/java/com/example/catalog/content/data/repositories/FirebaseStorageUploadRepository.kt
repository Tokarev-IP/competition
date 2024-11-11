package com.example.catalog.content.data.repositories

import android.net.Uri
import com.google.firebase.Firebase
import com.google.firebase.storage.storage
import javax.inject.Inject

class FirebaseStorageUploadRepository @Inject constructor() : FirebaseStorageUploadInterface {

    private val storageRef = Firebase.storage.reference

    override fun uploadFileUsingUri(
        pathString: String,
        fileUri: Uri,
        onSuccess: () -> Unit,
        onFailure: (e: Exception) -> Unit,
    ) {
        storageRef.child(pathString).putFile(fileUri)
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener { e: Exception ->
                onFailure(e)
            }
    }

    override fun uploadFileUsingByteArray(
        pathString: String,
        bytes: ByteArray,
        onSuccess: () -> Unit,
        onFailure: (e: Exception) -> Unit,
    ) {
        storageRef.child(pathString).putBytes(bytes)
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener { e: Exception ->
                onFailure(e)
            }
    }
}

interface FirebaseStorageUploadInterface {

    fun uploadFileUsingUri(
        pathString: String,
        fileUri: Uri,
        onSuccess: () -> Unit,
        onFailure: (e: Exception) -> Unit,
    )

    fun uploadFileUsingByteArray(
        pathString: String,
        bytes: ByteArray,
        onSuccess: () -> Unit,
        onFailure: (e: Exception) -> Unit,
    )
}