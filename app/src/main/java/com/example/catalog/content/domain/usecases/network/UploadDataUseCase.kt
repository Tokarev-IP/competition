package com.example.catalog.content.domain.usecases.network

import com.example.catalog.content.data.interfaces.FirestoreUploadInterface
import com.example.catalog.content.domain.data.DishDataFirebase
import com.example.catalog.content.domain.data.MenuIdFirebase
import com.example.catalog.content.domain.data.MenuInfoFirebase
import com.example.catalog.content.domain.data.SectionDataFirebase
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class UploadDataUseCase @Inject constructor(
    private val firestoreAddInterface: FirestoreUploadInterface,
) : UploadDataUseCaseInterface {

    override suspend fun uploadMenuId(
        collection: String,
        userId: String,
        menuIdFirebase: MenuIdFirebase,
    ) {
        return suspendCancellableCoroutine { continuation ->
            firestoreAddInterface.uploadOneCollectionData(
                data = menuIdFirebase,
                collection = collection,
                documentId = userId,
                onSuccess = {
                    continuation.resume(Unit)
                },
                onFailure = { e: Exception ->
                    continuation.resumeWithException(e)
                }
            )
        }
    }

    override suspend fun uploadMenuInfoData(
        collection: String,
        data: MenuInfoFirebase,
        menuId: String,
    ) {
        return suspendCancellableCoroutine { continuation ->
            firestoreAddInterface.uploadOneCollectionData(
                data = data,
                collection = collection,
                documentId = menuId,
                onSuccess = {
                    continuation.resume(Unit)
                },
                onFailure = { e: Exception ->
                    continuation.resumeWithException(e)
                }
            )
        }
    }

    override suspend fun uploadMenuDishData(
        collection1: String,
        collection2: String,
        data: DishDataFirebase,
        menuId: String,
        documentId: String,
    ) {
        return suspendCancellableCoroutine { continuation ->
            firestoreAddInterface.uploadTwoCollectionData(
                data = data,
                collection1 = collection1,
                collection2 = collection2,
                documentPath = menuId,
                documentId = documentId,
                onSuccess = {
                    continuation.resume(Unit)
                },
                onFailure = { e: Exception ->
                    continuation.resumeWithException(e)
                }
            )
        }
    }

    override suspend fun uploadMenuSection(
        collection1: String,
        collection2: String,
        data: SectionDataFirebase,
        menuId: String,
        documentId: String
    ) {
        return suspendCancellableCoroutine { continuation ->
            firestoreAddInterface.uploadTwoCollectionData(
                data = data,
                collection1 = collection1,
                collection2 = collection2,
                documentPath = menuId,
                documentId = documentId,
                onSuccess = {
                    continuation.resume(Unit)
                },
                onFailure = { e: Exception ->
                    continuation.resumeWithException(e)
                }
            )
        }
    }
}

interface UploadDataUseCaseInterface {
    suspend fun uploadMenuId(
        collection: String = "id",
        userId: String,
        menuIdFirebase: MenuIdFirebase,
    )

    suspend fun uploadMenuInfoData(
        collection: String = "menu",
        data: MenuInfoFirebase,
        menuId: String,
    )

    suspend fun uploadMenuDishData(
        collection1: String = "data",
        collection2: String = "menu",
        data: DishDataFirebase,
        menuId: String,
        documentId: String,
    )

    suspend fun uploadMenuSection(
        collection1: String = "data",
        collection2: String = "section",
        data: SectionDataFirebase,
        menuId: String,
        documentId: String,
    )
}