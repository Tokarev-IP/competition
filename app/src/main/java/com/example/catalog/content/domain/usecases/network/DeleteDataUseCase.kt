package com.example.catalog.content.domain.usecases.network

import com.example.catalog.content.data.interfaces.FirestoreDeleteInterface
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class DeleteDataUseCase @Inject constructor(
    private val firestoreDeleteInterface: FirestoreDeleteInterface,
) : DeleteDataUseCaseInterface {

    override suspend fun deleteMenuDish(
        collection1: String,
        collection2: String,
        menuId: String,
        dishId: String
    ) {
        return suspendCancellableCoroutine<Unit> { continuation ->
            firestoreDeleteInterface.deleteDocumentTwoCollections(
                collection1 = collection1,
                collection2 = collection2,
                document1 = menuId,
                document2 = dishId,
                onSuccess = {
                    continuation.resume(Unit)
                },
                onFailure = { e: Exception ->
                    continuation.resumeWithException(e)
                },
            )
        }
    }

    override suspend fun deleteMenuSection(
        collection1: String,
        collection2: String,
        menuId: String,
        sectionId: String
    ) {
        return suspendCancellableCoroutine<Unit> { continuation ->
            firestoreDeleteInterface.deleteDocumentTwoCollections(
                collection1 = collection1,
                collection2 = collection2,
                document1 = menuId,
                document2 = sectionId,
                onSuccess = {
                    continuation.resume(Unit)
                },
                onFailure = { e: Exception ->
                    continuation.resumeWithException(e)
                },
            )
        }
    }
}

interface DeleteDataUseCaseInterface {
    suspend fun deleteMenuDish(
        collection1: String = "data",
        collection2: String = "menu",
        menuId: String,
        dishId: String,
    )

    suspend fun deleteMenuSection(
        collection1: String = "section",
        collection2: String = "menu",
        menuId: String,
        sectionId: String,
    )
}