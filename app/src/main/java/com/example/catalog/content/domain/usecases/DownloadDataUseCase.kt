package com.example.catalog.content.domain.usecases

import com.example.catalog.content.data.interfaces.FirestoreDownloadInterface
import com.example.catalog.content.domain.data.DishData
import com.example.catalog.content.domain.data.MenuId
import com.example.catalog.content.domain.data.MenuInfo
import com.example.catalog.content.domain.interfaces.DownloadDataUseCaseInterface
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import javax.inject.Inject

class DownloadDataUseCase @Inject constructor(
    private val firestoreDownloadInterface: FirestoreDownloadInterface,
) : DownloadDataUseCaseInterface {

    override fun downloadMenuId(
        collection: String,
        userId: String,
        onSuccess: (menuId: MenuId) -> Unit,
        onNull: () -> Unit,
        onFailure: (msg: String) -> Unit
    ) {
        firestoreDownloadInterface.downloadDocumentFromOneCollection(
            collection = collection,
            documentPath = userId,
            onSuccess = { result: DocumentSnapshot ->
                val document: MenuId? = result.toObject(MenuId::class.java)
                if (document != null) {
                    onSuccess(document)
                } else {
                    onNull()
                }
            },
            onFailure = { e: Exception ->
                onFailure(e.message.toString())
            }
        )
    }

    override fun downloadMenuInfoData(
        collection: String,
        menuId: String,
        onSuccess: (menuInfo: MenuInfo) -> Unit,
        onNull: () -> Unit,
        onFailure: (msg: String) -> Unit
    ) {
        firestoreDownloadInterface.downloadDocumentFromOneCollection(
            collection = collection,
            documentPath = menuId,
            onSuccess = { result: DocumentSnapshot ->
                val document: MenuInfo? = result.toObject(MenuInfo::class.java)
                if (document != null) {
                    onSuccess(document)
                } else {
                    onNull()
                }
            },
            onFailure = { e: Exception ->
                onFailure(e.message.toString())
            }
        )
    }

    override fun downloadMenuDishListData(
        collection1: String,
        collection2: String,
        menuId: String,
        onSuccess: (List<DishData>) -> Unit,
        onFailure: (msg: String) -> Unit
    ) {
        firestoreDownloadInterface.downloadDataFromTwoCollection(
            collection1 = collection1,
            collection2 = collection2,
            documentPath = menuId,
            onSuccess = { result: QuerySnapshot ->
                val dishList = mutableListOf<DishData>()
                for (document in result) {
                    if (document != null) {
                        val data = document.toObject(DishData::class.java)
                        dishList.add(data)
                    }
                }
                onSuccess(dishList)
            },
            onFailure = { e: Exception ->
                onFailure(e.message.toString())
            }
        )
    }

}