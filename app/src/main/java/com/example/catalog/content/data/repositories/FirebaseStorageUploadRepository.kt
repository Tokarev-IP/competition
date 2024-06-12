package com.example.catalog.content.data.repositories

import android.net.Uri
import com.example.catalog.content.data.interfaces.FirebaseStorageUploadInterface
import com.google.firebase.Firebase
import com.google.firebase.storage.UploadTask
import com.google.firebase.storage.storage
import javax.inject.Inject

class FirebaseStorageUploadRepository @Inject constructor() : FirebaseStorageUploadInterface {

    private val storageRef = Firebase.storage.reference

    override fun uploadFileUsingUri(
        pathString: String,
        fileUri: Uri,
        onSuccess: (task: UploadTask.TaskSnapshot) -> Unit,
        onFailure: (e: Exception) -> Unit,
    ) {
        storageRef.child(pathString).putFile(fileUri)
            .addOnSuccessListener { task: UploadTask.TaskSnapshot ->
                onSuccess(task)
            }
            .addOnFailureListener { e: Exception ->
                onFailure(e)
            }
    }

    override fun uploadFileUsingByteArray(
        pathString: String,
        bytes: ByteArray,
        onSuccess: (task: UploadTask.TaskSnapshot) -> Unit,
        onFailure: (e: Exception) -> Unit,
    ) {
        storageRef.child(pathString).putBytes(bytes)
            .addOnSuccessListener { task: UploadTask.TaskSnapshot ->
                onSuccess(task)
            }
            .addOnFailureListener { e: Exception ->
                onFailure(e)
            }
    }
}