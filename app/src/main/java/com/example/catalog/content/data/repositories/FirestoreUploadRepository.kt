package com.example.catalog.content.data.repositories

import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import javax.inject.Inject

class FirestoreUploadRepository @Inject constructor() : FirestoreUploadInterface {

    private val db: FirebaseFirestore = Firebase.firestore

    override fun <T : Any> uploadOneCollectionData(
        data: T,
        collection: String,
        documentId: String,
        onSuccess: () -> Unit,
        onFailure: (e: Exception) -> Unit,
    ) {
        db.collection(collection).document(documentId)
            .set(data)
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener { e: Exception ->
                onFailure(e)
            }
    }

    override fun <T : Any> uploadTwoCollectionData(
        data: T,
        collection1: String,
        collection2: String,
        documentPath: String,
        documentId: String,
        onSuccess: () -> Unit,
        onFailure: (e: Exception) -> Unit,
    ) {
        db.collection(collection1).document(documentPath).collection(collection2).document(documentId)
            .set(data)
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener { e: Exception ->
                onFailure(e)
            }
    }
}

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