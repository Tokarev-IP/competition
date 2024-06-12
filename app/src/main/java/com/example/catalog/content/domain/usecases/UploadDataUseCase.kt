package com.example.catalog.content.domain.usecases

import com.example.catalog.content.data.interfaces.FirestoreUploadInterface
import com.example.catalog.content.domain.data.DishData
import com.example.catalog.content.domain.data.MenuId
import com.example.catalog.content.domain.data.MenuInfo
import com.example.catalog.content.domain.interfaces.UploadDataUseCaseInterface
import javax.inject.Inject

class UploadDataUseCase @Inject constructor(
    private val firestoreAddInterface: FirestoreUploadInterface,
) : UploadDataUseCaseInterface {

    override fun uploadMenuId(
        collection: String,
        userId: String,
        menuId: MenuId,
        onSuccess: () -> Unit,
        onFailure: (msg: String) -> Unit
    ) {
        firestoreAddInterface.uploadOneCollectionData(
            data = menuId,
            collection = collection,
            documentId = userId,
            onSuccess = { onSuccess() },
            onFailure = { e: Exception ->
                onFailure(e.cause.toString())
            }
        )
    }

    override fun uploadMenuInfoData(
        collection: String,
        data: MenuInfo,
        menuId: String,
        onSuccess: () -> Unit,
        onFailure: (msg: String) -> Unit
    ) {
        firestoreAddInterface.uploadOneCollectionData(
            data = data,
            collection = collection,
            documentId = menuId,
            onSuccess = { onSuccess() },
            onFailure = { e: Exception ->
                onFailure(e.message.toString())
            }
        )
    }

    override fun uploadMenuDishData(
        collection1: String,
        collection2: String,
        data: DishData,
        menuId: String,
        documentId: String,
        onSuccess: () -> Unit,
        onFailure: (msg: String) -> Unit
    ) {
        firestoreAddInterface.uploadTwoCollectionData(
            data = data,
            collection1 = collection1,
            collection2 = collection2,
            documentPath = menuId,
            documentId = documentId,
            onSuccess = { onSuccess() },
            onFailure = { e: Exception ->
                onFailure(e.message.toString())
            }
        )
    }
}