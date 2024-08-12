package com.example.catalog.content.data.interfaces

import android.net.Uri
import com.google.firebase.storage.StorageMetadata

interface FirebaseStorageDownloadInterface {

    fun downloadUriOfFile(
        pathString: String,
        onSuccess: (uri: Uri?) -> Unit,
        onFailure: (e: Exception) -> Unit,
    )

    fun downloadImageFile(
        pathString: String,
        onSuccess: (byteArray: ByteArray) -> Unit,
        onFailure: (e: Exception) -> Unit,
    )

    fun getMetadataOfFile(
        pathString: String,
        onSuccess: (metadata: StorageMetadata) -> Unit,
        onFailure: (e: Exception) -> Unit,
    )

}