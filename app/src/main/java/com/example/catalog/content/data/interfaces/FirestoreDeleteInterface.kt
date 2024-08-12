package com.example.catalog.content.data.interfaces

interface FirestoreDeleteInterface {

    fun deleteDocumentOneCollection(
        collection1: String,
        document1: String,
        onSuccess: () -> Unit,
        onFailure: (e: Exception) -> Unit,
    )

    fun deleteDocumentTwoCollections(
        collection1: String,
        document1: String,
        collection2: String,
        document2: String,
        onSuccess: () -> Unit,
        onFailure: (e: Exception) -> Unit,
    )
}