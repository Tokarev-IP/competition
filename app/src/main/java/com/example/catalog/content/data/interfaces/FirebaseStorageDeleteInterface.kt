package com.example.catalog.content.data.interfaces

interface FirebaseStorageDeleteInterface {

    fun deleteFile(
        pathString: String,
        onSuccess: () -> Unit,
        onFailure: (e: Exception) -> Unit,
    )
}