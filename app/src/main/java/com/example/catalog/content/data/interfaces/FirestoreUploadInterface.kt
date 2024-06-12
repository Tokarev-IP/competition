package com.example.catalog.content.data.interfaces

interface FirestoreUploadInterface {

    fun <T : Any> uploadOneCollectionData(
        data: T,
        collection: String,
        documentId: String,
        onSuccess: () -> Unit,
        onFailure: (e: Exception) -> Unit,
    )

    fun <T : Any> uploadTwoCollectionData(
        data: T,
        collection1: String,
        collection2: String,
        documentPath: String,
        documentId: String,
        onSuccess: () -> Unit,
        onFailure: (e: Exception) -> Unit,
    )

}