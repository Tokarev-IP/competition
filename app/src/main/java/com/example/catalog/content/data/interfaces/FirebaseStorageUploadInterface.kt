package com.example.catalog.content.data.interfaces

import android.net.Uri
import com.google.firebase.storage.UploadTask

interface FirebaseStorageUploadInterface {

    fun uploadFileUsingUri(
        pathString: String,
        fileUri: Uri,
        onSuccess: (task: UploadTask.TaskSnapshot) -> Unit,
        onFailure: (e: Exception) -> Unit,
    )

    fun uploadFileUsingByteArray(
        pathString: String,
        bytes: ByteArray,
        onSuccess: (task: UploadTask.TaskSnapshot) -> Unit,
        onFailure: (e: Exception) -> Unit,
    )
}