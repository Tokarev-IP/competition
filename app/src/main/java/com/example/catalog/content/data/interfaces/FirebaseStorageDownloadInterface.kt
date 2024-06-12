package com.example.catalog.content.data.interfaces

import android.net.Uri

interface FirebaseStorageDownloadInterface {

    fun downloadUriOfFile(
        pathString: String,
        onSuccess: (uri: Uri?) -> Unit,
        onFailure: (e: Exception) -> Unit,
    )
}