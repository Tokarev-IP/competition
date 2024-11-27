package com.example.catalog.content.domain.usecases.busket

//class UploadDataUseCase @Inject constructor(
//    private val firestoreUploadInterface: FirestoreUploadInterface,
//) : UploadDataUseCaseInterface {
//
//    override suspend fun uploadMenuId(
//        collection: String,
//        userId: String,
//        menuIdFirebase: MenuIdFirebase,
//    ) {
//        return suspendCancellableCoroutine { continuation ->
//            firestoreUploadInterface.uploadOneCollectionData(
//                data = menuIdFirebase,
//                collection = collection,
//                documentId = userId,
//                onSuccess = {
//                    continuation.resume(Unit)
//                },
//                onFailure = { e: Exception ->
//                    continuation.resumeWithException(e)
//                }
//            )
//        }
//    }
//
//    override suspend fun uploadMenuInfoData(
//        collection: String,
//        data: MenuInfoFirebase,
//        menuId: String,
//    ) {
//        return suspendCancellableCoroutine { continuation ->
//            firestoreUploadInterface.uploadOneCollectionData(
//                data = data,
//                collection = collection,
//                documentId = menuId,
//                onSuccess = {
//                    continuation.resume(Unit)
//                },
//                onFailure = { e: Exception ->
//                    continuation.resumeWithException(e)
//                }
//            )
//        }
//    }
//
//    override suspend fun uploadMenuDishData(
//        collection1: String,
//        collection2: String,
//        data: DishDataFirebase,
//        menuId: String,
//        dishId: String,
//    ) {
//        return suspendCancellableCoroutine { continuation ->
//            firestoreUploadInterface.uploadTwoCollectionData(
//                data = data,
//                collection1 = collection1,
//                collection2 = collection2,
//                documentPath = menuId,
//                documentId = dishId,
//                onSuccess = {
//                    continuation.resume(Unit)
//                },
//                onFailure = { e: Exception ->
//                    continuation.resumeWithException(e)
//                }
//            )
//        }
//    }
//
//    override suspend fun uploadMenuSectionData(
//        collection1: String,
//        collection2: String,
//        data: SectionDataFirebase,
//        menuId: String,
//        sectionId: String
//    ) {
//        return suspendCancellableCoroutine { continuation ->
//            firestoreUploadInterface.uploadTwoCollectionData(
//                data = data,
//                collection1 = collection1,
//                collection2 = collection2,
//                documentPath = menuId,
//                documentId = sectionId,
//                onSuccess = {
//                    continuation.resume(Unit)
//                },
//                onFailure = { e: Exception ->
//                    continuation.resumeWithException(e)
//                }
//            )
//        }
//    }
//
//    override suspend fun uploadInfoImageData(
//        collection1: String,
//        collection2: String,
//        data: InfoImageFirebase,
//        menuId: String,
//        imageId: String
//    ) {
//        return suspendCancellableCoroutine { continuation ->
//            firestoreUploadInterface.uploadTwoCollectionData(
//                data = data,
//                collection1 = collection1,
//                collection2 = collection2,
//                documentPath = menuId,
//                documentId = imageId,
//                onSuccess = {
//                    continuation.resume(Unit)
//                },
//                onFailure = { e: Exception ->
//                    continuation.resumeWithException(e)
//                }
//            )
//        }
//    }
//}
//
//interface UploadDataUseCaseInterface {
//    suspend fun uploadMenuId(
//        collection: String = "id",
//        userId: String,
//        menuIdFirebase: MenuIdFirebase,
//    )
//
//    suspend fun uploadMenuInfoData(
//        collection: String = "info",
//        data: MenuInfoFirebase,
//        menuId: String,
//    )
//
//    suspend fun uploadMenuDishData(
//        collection1: String = "data",
//        collection2: String = "menu",
//        data: DishDataFirebase,
//        menuId: String,
//        dishId: String,
//    )
//
//    suspend fun uploadMenuSectionData(
//        collection1: String = "data",
//        collection2: String = "section",
//        data: SectionDataFirebase,
//        menuId: String,
//        sectionId: String,
//    )
//
//    suspend fun uploadInfoImageData(
//        collection1: String = "data",
//        collection2: String = "image",
//        data: InfoImageFirebase,
//        menuId: String,
//        imageId: String,
//    )
//}