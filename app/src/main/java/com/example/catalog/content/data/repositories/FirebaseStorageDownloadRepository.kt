package com.example.catalog.content.data.repositories

import android.net.Uri
import com.example.catalog.content.data.interfaces.FirebaseStorageDownloadInterface
import com.google.firebase.Firebase
import com.google.firebase.storage.storage
import javax.inject.Inject

class FirebaseStorageDownloadRepository @Inject constructor(): FirebaseStorageDownloadInterface {

    private val storageRef = Firebase.storage.reference

    override fun downloadUriOfFile(
        pathString: String,
        onSuccess: (uri: Uri?) -> Unit,
        onFailure: (e: Exception) -> Unit,
    ) {
        storageRef.child(pathString).downloadUrl
            .addOnSuccessListener { uri: Uri? ->
                onSuccess(uri)
            }
            .addOnFailureListener { e: Exception ->
                onFailure(e)
            }
    }
}