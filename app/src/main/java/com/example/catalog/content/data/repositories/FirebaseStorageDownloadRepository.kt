package com.example.catalog.content.data.repositories

import android.net.Uri
import com.example.catalog.content.data.interfaces.FirebaseStorageDownloadInterface
import com.google.firebase.Firebase
import com.google.firebase.storage.StorageMetadata
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

    override fun downloadImageFile(
        pathString: String,
        onSuccess: (byteArray: ByteArray) -> Unit,
        onFailure: (e: Exception) -> Unit
    ) {
        storageRef.child(pathString).getBytes(10240 * 102400)
            .addOnSuccessListener { result: ByteArray ->
                onSuccess(result)
            }
            .addOnFailureListener { e: Exception ->
                onFailure(e)
            }
    }

    override fun getMetadataOfFile(
        pathString: String,
        onSuccess: (metadata: StorageMetadata) -> Unit,
        onFailure: (e: Exception) -> Unit
    ) {
        storageRef.child(pathString).metadata
            .addOnSuccessListener { metadata: StorageMetadata ->
                onSuccess(metadata)
            }
            .addOnFailureListener { e: Exception ->
                onFailure(e)
            }
    }
}