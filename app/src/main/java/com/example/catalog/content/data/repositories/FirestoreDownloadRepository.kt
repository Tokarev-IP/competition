package com.example.catalog.content.data.repositories

import com.example.catalog.content.domain.data.DishDataFirebase
import com.example.catalog.content.domain.data.InfoImageFirebase
import com.example.catalog.content.domain.data.MenuIdFirebase
import com.example.catalog.content.domain.data.MenuInfoFirebase
import com.example.catalog.content.domain.data.SectionDataFirebase
import com.google.firebase.Firebase
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.firestore
import javax.inject.Inject

class FirestoreDownloadRepository @Inject constructor() : FirebaseDownloadAbstract(), FirestoreDownloadInterface {

    override fun downloadMenuIdDataFromFirebase(
        collection: String,
        userId: String,
        onSuccess: (result: MenuIdFirebase?) -> Unit,
        onFailure: (e: Exception) -> Unit
    ) {
        downloadDocumentFromOneCollection<MenuIdFirebase>(
            collection = collection,
            documentPath = userId,
            onSuccess = { result: MenuIdFirebase? ->
                onSuccess(result)
            },
            onFailure = { error: Exception ->
                onFailure(error)
            }
        )
    }

    override fun downloadMenuInfoDataFromFirebase(
        collection: String,
        menuId: String,
        onSuccess: (result: MenuInfoFirebase?) -> Unit,
        onFailure: (e: Exception) -> Unit
    ) {
        downloadDocumentFromOneCollection<MenuInfoFirebase>(
            collection = collection,
            documentPath = menuId,
            onSuccess = { result: MenuInfoFirebase? ->
                onSuccess(result)
            },
            onFailure = { e: Exception ->
                onFailure(e)
            }
        )
    }

    override fun downloadMenuDishListDataFromFirebase(
        collection1: String,
        collection2: String,
        menuId: String,
        onSuccess: (result: List<DishDataFirebase>) -> Unit,
        onFailure: (e: Exception) -> Unit
    ) {
        downloadListDocumentsFromTwoCollection<DishDataFirebase>(
            collection1 = collection1,
            collection2 = collection2,
            documentPath = menuId,
            onSuccess = { result: List<DishDataFirebase> ->
                onSuccess(result)
            },
            onFailure = { e: Exception ->
                onFailure(e)
            }
        )
    }

    override fun downloadMenuSectionListDataFromFirebase(
        collection1: String,
        collection2: String,
        menuId: String,
        onSuccess: (result: List<SectionDataFirebase>) -> Unit,
        onFailure: (e: Exception) -> Unit
    ) {
        downloadListDocumentsFromTwoCollection<SectionDataFirebase>(
            collection1 = collection1,
            collection2 = collection2,
            documentPath = menuId,
            onSuccess = { result: List<SectionDataFirebase> ->
                onSuccess(result)
            },
            onFailure = { e: Exception ->
                onFailure(e)
            }
        )
    }

    override fun downloadInfoImageListDataFromFirebase(
        collection1: String,
        collection2: String,
        menuId: String,
        onSuccess: (result: List<InfoImageFirebase>) -> Unit,
        onFailure: (e: Exception) -> Unit
    ) {
        downloadListDocumentsFromTwoCollection<InfoImageFirebase>(
            collection1 = collection1,
            collection2 = collection2,
            documentPath = menuId,
            onSuccess = { result: List<InfoImageFirebase> ->
                onSuccess(result)
            },
            onFailure = { e: Exception ->
                onFailure(e)
            }
        )
    }
}

abstract class FirebaseDownloadAbstract {

    inline fun <reified T> downloadListDocumentsFromOneCollection(
        collection: String,
        crossinline onSuccess: (result: List<T>) -> Unit,
        crossinline onFailure: (e: Exception) -> Unit,
    ) {
        Firebase.firestore.collection(collection)
            .get()
            .addOnSuccessListener { querySnapshot: QuerySnapshot ->
                val documentSnapshotList = mutableListOf<T>()
                for (data in querySnapshot) {
                    val newData = data.toObject(T::class.java)
                    documentSnapshotList.add(newData as T)
                }
                onSuccess(documentSnapshotList)

            }
            .addOnFailureListener { e: Exception ->
                onFailure(e)
            }
    }

    inline fun <reified T> downloadListDocumentsFromTwoCollection(
        collection1: String,
        collection2: String,
        documentPath: String,
        crossinline onSuccess: (result: List<T>) -> Unit,
        crossinline onFailure: (e: Exception) -> Unit
    ) {
        Firebase.firestore.collection(collection1).document(documentPath).collection(collection2)
            .get()
            .addOnSuccessListener { querySnapshot: QuerySnapshot ->
                val documentSnapshotList = mutableListOf<T>()
                for (data in querySnapshot) {
                    val newData = data.toObject(T::class.java)
                    documentSnapshotList.add(newData)
                }
                onSuccess(documentSnapshotList)
            }
            .addOnFailureListener { e: Exception ->
                onFailure(e)
            }
    }

    inline fun <reified T> downloadDocumentFromOneCollection(
        collection: String,
        documentPath: String,
        crossinline onSuccess: (result: T?) -> Unit,
        crossinline onFailure: (e: Exception) -> Unit
    ) {
        Firebase.firestore.collection(collection).document(documentPath)
            .get()
            .addOnSuccessListener { document: DocumentSnapshot ->
                onSuccess(document.toObject(T::class.java))
            }
            .addOnFailureListener { e: Exception ->
                onFailure(e)
            }
    }

    inline fun <reified T> downloadDocumentFromTwoCollection(
        collection1: String,
        collection2: String,
        documentPath1: String,
        documentPath2: String,
        crossinline onSuccess: (result: T?) -> Unit,
        crossinline onFailure: (e: Exception) -> Unit
    ) {
        Firebase.firestore.collection(collection1).document(documentPath1)
            .collection(collection2).document(documentPath2)
            .get()
            .addOnSuccessListener { document: DocumentSnapshot ->
                onSuccess(document.toObject(T::class.java))
            }
            .addOnFailureListener { e: Exception ->
                onFailure(e)
            }
    }
}

interface FirestoreDownloadInterface {

    fun downloadMenuIdDataFromFirebase(
        collection: String = "id",
        userId: String,
        onSuccess: (result: MenuIdFirebase?) -> Unit,
        onFailure: (e: Exception) -> Unit,
    )

    fun downloadMenuInfoDataFromFirebase(
        collection: String = "info",
        menuId: String,
        onSuccess: (result: MenuInfoFirebase?) -> Unit,
        onFailure: (e: Exception) -> Unit,
    )

    fun downloadMenuDishListDataFromFirebase(
        collection1: String = "data",
        collection2: String = "menu",
        menuId: String,
        onSuccess: (result: List<DishDataFirebase>) -> Unit,
        onFailure: (e: Exception) -> Unit,
    )

    fun downloadMenuSectionListDataFromFirebase(
        collection1: String = "data",
        collection2: String = "section",
        menuId: String,
        onSuccess: (result: List<SectionDataFirebase>) -> Unit,
        onFailure: (e: Exception) -> Unit,
    )

    fun downloadInfoImageListDataFromFirebase(
        collection1: String = "data",
        collection2: String = "image",
        menuId: String,
        onSuccess: (result: List<InfoImageFirebase>) -> Unit,
        onFailure: (e: Exception) -> Unit,
    )
}