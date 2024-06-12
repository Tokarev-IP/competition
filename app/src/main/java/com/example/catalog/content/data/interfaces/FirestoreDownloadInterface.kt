package com.example.catalog.content.data.interfaces

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot

interface FirestoreDownloadInterface {

    fun downloadDataFromOneCollection(
        collection: String,
        onSuccess: (result: QuerySnapshot) -> Unit,
        onFailure: (e: Exception) -> Unit,
    )

    fun downloadDataFromTwoCollection(
        collection1: String,
        collection2: String,
        documentPath: String,
        onSuccess: (result: QuerySnapshot) -> Unit,
        onFailure: (e: Exception) -> Unit,
    )

    fun downloadDocumentFromOneCollection(
        collection: String,
        documentPath: String,
        onSuccess: (result: DocumentSnapshot) -> Unit,
        onFailure: (e: Exception) -> Unit,
    )

    fun downloadDocumentFromTwoCollection(
        collection1: String,
        collection2: String,
        documentPath1: String,
        documentPath2: String,
        onSuccess: (result: DocumentSnapshot) -> Unit,
        onFailure: (e: Exception) -> Unit,
    )
}