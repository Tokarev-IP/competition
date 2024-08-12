package com.example.catalog.content.data.repositories

import com.example.catalog.content.data.interfaces.FirestoreDownloadInterface
import com.google.firebase.Firebase
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.firestore
import javax.inject.Inject

class FirestoreDownloadRepository @Inject constructor() : FirestoreDownloadInterface {

    private val db: FirebaseFirestore = Firebase.firestore

    override fun downloadDataFromOneCollection(
        collection: String,
        onSuccess: (result: QuerySnapshot) -> Unit,
        onFailure: (e: Exception) -> Unit,
    ) {
        db.collection(collection)
            .get()
            .addOnSuccessListener { it: QuerySnapshot ->
                onSuccess(it)
            }
            .addOnFailureListener { e: Exception ->
                onFailure(e)
            }
    }

    override fun downloadDataFromTwoCollection(
        collection1: String,
        collection2: String,
        documentPath: String,
        onSuccess: (result: QuerySnapshot) -> Unit,
        onFailure: (e: Exception) -> Unit,
    ) {
        db.collection(collection1).document(documentPath).collection(collection2)
            .get()
            .addOnSuccessListener { it: QuerySnapshot ->
                onSuccess(it)
            }
            .addOnFailureListener { e: Exception ->
                onFailure(e)
            }
    }

    override fun downloadDocumentFromOneCollection(
        collection: String,
        documentPath: String,
        onSuccess: (result: DocumentSnapshot) -> Unit,
        onFailure: (e: Exception) -> Unit,
    ) {
        db.collection(collection).document(documentPath)
            .get()
            .addOnSuccessListener { document: DocumentSnapshot ->
                onSuccess(document)
            }
            .addOnFailureListener { e: Exception ->
                onFailure(e)
            }
    }

    override fun downloadDocumentFromTwoCollection(
        collection1: String,
        collection2: String,
        documentPath1: String,
        documentPath2: String,
        onSuccess: (result: DocumentSnapshot) -> Unit,
        onFailure: (e: Exception) -> Unit,
    ) {
        db.collection(collection1).document(documentPath1)
            .collection(collection2).document(documentPath2)
            .get()
            .addOnSuccessListener { document: DocumentSnapshot ->
                onSuccess(document)
            }
            .addOnFailureListener { e: Exception ->
                onFailure(e)
            }
    }
}