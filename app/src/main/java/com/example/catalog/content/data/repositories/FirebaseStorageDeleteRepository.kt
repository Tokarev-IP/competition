package com.example.catalog.content.data.repositories

import com.example.catalog.content.data.interfaces.FirebaseStorageDeleteInterface
import com.google.firebase.Firebase
import com.google.firebase.storage.storage
import javax.inject.Inject

class FirebaseStorageDeleteRepository @Inject constructor(): FirebaseStorageDeleteInterface {

    private val storageRef = Firebase.storage.reference

    override fun deleteFile(
        pathString: String,
        onSuccess: () -> Unit,
        onFailure: (e: Exception) -> Unit
    ) {
        storageRef.child(pathString).delete()
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener { e: Exception ->
                onFailure(e)
            }
    }

}