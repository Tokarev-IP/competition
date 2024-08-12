package com.example.catalog.content.data.repositories

import com.example.catalog.content.data.interfaces.FirestoreDeleteInterface
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import javax.inject.Inject

class FirestoreDeleteRepository @Inject constructor(): FirestoreDeleteInterface {

    private val db: FirebaseFirestore = Firebase.firestore

    override fun deleteDocumentOneCollection(
        collection1: String,
        document1: String,
        onSuccess: () -> Unit,
        onFailure: (e: Exception) -> Unit
    ) {
        db.collection(collection1).document(document1)
            .delete()
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener { e: Exception ->
                onFailure(e)
            }
    }

    override fun deleteDocumentTwoCollections(
        collection1: String,
        document1: String,
        collection2: String,
        document2: String,
        onSuccess: () -> Unit,
        onFailure: (e: Exception) -> Unit
    ) {
        db.collection(collection1).document(document1).collection(collection2).document(document2)
            .delete()
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener { e: Exception ->
                onFailure(e)
            }
    }

}