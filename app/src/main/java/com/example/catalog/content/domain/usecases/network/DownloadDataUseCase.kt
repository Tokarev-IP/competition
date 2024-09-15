package com.example.catalog.content.domain.usecases.network

import com.example.catalog.content.data.interfaces.FirestoreDownloadInterface
import com.example.catalog.content.domain.data.DishData
import com.example.catalog.content.domain.data.DishDataFirebase
import com.example.catalog.content.domain.data.InfoImageData
import com.example.catalog.content.domain.data.InfoImageFirebase
import com.example.catalog.content.domain.data.MenuIdFirebase
import com.example.catalog.content.domain.data.MenuInfoFirebase
import com.example.catalog.content.domain.data.SectionData
import com.example.catalog.content.domain.data.SectionDataFirebase
import com.example.catalog.content.domain.extensions.toDishData
import com.example.catalog.content.domain.extensions.toInfoImageData
import com.example.catalog.content.domain.extensions.toSectionData
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class DownloadDataUseCase @Inject constructor(
    private val firestoreDownloadInterface: FirestoreDownloadInterface,
) : DownloadDataUseCaseInterface {

    override suspend fun downloadMenuIdData(
        collection: String,
        userId: String,
    ): MenuIdFirebase? {
        return suspendCancellableCoroutine { continuation ->
            firestoreDownloadInterface.downloadDocumentFromOneCollection(
                collection = collection,
                documentPath = userId,
                onSuccess = { result: DocumentSnapshot ->
                    val document: MenuIdFirebase? = result.toObject(MenuIdFirebase::class.java)
                    continuation.resume(document)
                },
                onFailure = { error: Exception ->
                    continuation.resumeWithException(error)
                }
            )
        }
    }

    override suspend fun downloadMenuInfoData(
        collection: String,
        menuId: String,
    ): MenuInfoFirebase? {
        return suspendCancellableCoroutine { continuation ->
            firestoreDownloadInterface.downloadDocumentFromOneCollection(
                collection = collection,
                documentPath = menuId,
                onSuccess = { result: DocumentSnapshot ->
                    val document: MenuInfoFirebase? = result.toObject(MenuInfoFirebase::class.java)
                    continuation.resume(document)
                },
                onFailure = { e: Exception ->
                    continuation.resumeWithException(e)
                }
            )
        }
    }

    override suspend fun downloadMenuDishListData(
        collection1: String,
        collection2: String,
        menuId: String,
    ): List<DishData> {
        return suspendCancellableCoroutine { continuation ->
            firestoreDownloadInterface.downloadDataFromTwoCollection(
                collection1 = collection1,
                collection2 = collection2,
                documentPath = menuId,
                onSuccess = { result: QuerySnapshot ->
                    val dishList = mutableListOf<DishData>()
                    for (document in result) {
                        if (document != null) {
                            val dataFirebase = document.toObject(DishDataFirebase::class.java)
                            val data = dataFirebase.toDishData()
                            dishList.add(data)
                        }
                    }
                    continuation.resume(dishList)
                },
                onFailure = { e: Exception ->
                    continuation.resumeWithException(e)
                }
            )
        }
    }

    override suspend fun downloadMenuSectionListData(
        collection1: String,
        collection2: String,
        menuId: String,
    ): List<SectionData> {
        return suspendCancellableCoroutine { continuation ->
            firestoreDownloadInterface.downloadDataFromTwoCollection(
                collection1 = collection1,
                collection2 = collection2,
                documentPath = menuId,
                onSuccess = { result: QuerySnapshot ->
                    val dataList = mutableListOf<SectionData>()
                    for (document in result) {
                        if (document != null) {
                            val dataFirebase = document.toObject(SectionDataFirebase::class.java)
                            val data = dataFirebase.toSectionData()
                            dataList.add(data)
                        }
                    }
                    continuation.resume(dataList)
                },
                onFailure = { e: Exception ->
                    continuation.resumeWithException(e)
                }
            )
        }
    }

    override suspend fun downloadInfoImageListData(
        collection1: String,
        collection2: String,
        menuId: String,
    ): List<InfoImageData> {
        return suspendCancellableCoroutine { continuation ->
            firestoreDownloadInterface.downloadDataFromTwoCollection(
                collection1 = collection1,
                collection2 = collection2,
                documentPath = menuId,
                onSuccess = { result: QuerySnapshot ->
                    val dataList = mutableListOf<InfoImageData>()
                    for (document in result) {
                        if (document != null) {
                            val dataFirebase = document.toObject(InfoImageFirebase::class.java)
                            dataList.add(dataFirebase.toInfoImageData())
                        }
                    }
                    continuation.resume(dataList)
                },
                onFailure = { e: Exception ->
                    continuation.resumeWithException(e)
                }
            )
        }
    }
}

interface DownloadDataUseCaseInterface {
    suspend fun downloadMenuIdData(
        collection: String = "id",
        userId: String,
    ): MenuIdFirebase?

    suspend fun downloadMenuInfoData(
        collection: String = "info",
        menuId: String,
    ): MenuInfoFirebase?

    suspend fun downloadMenuDishListData(
        collection1: String = "data",
        collection2: String = "menu",
        menuId: String,
    ): List<DishData>

    suspend fun downloadMenuSectionListData(
        collection1: String = "data",
        collection2: String = "section",
        menuId: String,
    ): List<SectionData>

    suspend fun downloadInfoImageListData(
        collection1: String = "data",
        collection2: String = "image",
        menuId: String,
    ): List<InfoImageData>
}