package com.example.catalog.content.domain.usecases.busket

//
//class DownloadDataUseCase @Inject constructor(
//    private val firestoreDownloadInterface: FirestoreDownloadInterface,
//) : DownloadDataUseCaseInterface {
//
//    override suspend fun downloadMenuIdData(
//        userId: String,
//    ): MenuIdFirebase? {
//        return suspendCancellableCoroutine { continuation ->
//            firestoreDownloadInterface.downloadMenuIdDataFromFirebase(
//                userId = userId,
//                onSuccess = { result: MenuIdFirebase? ->
//                    continuation.resume(result)
//                },
//                onFailure = { error: Exception ->
//                    continuation.resumeWithException(error)
//                }
//            )
//        }
//    }
//
//    override suspend fun downloadMenuInfoData(
//        menuId: String,
//    ): MenuInfoFirebase? {
//        return suspendCancellableCoroutine { continuation ->
//            firestoreDownloadInterface.downloadMenuInfoDataFromFirebase(
//                menuId = menuId,
//                onSuccess = { result: MenuInfoFirebase? ->
//                    continuation.resume(result)
//                },
//                onFailure = { e: Exception ->
//                    continuation.resumeWithException(e)
//                }
//            )
//        }
//    }
//
//    override suspend fun downloadMenuDishListData(
//        menuId: String,
//    ): List<DishData> {
//        return suspendCancellableCoroutine { continuation ->
//            firestoreDownloadInterface.downloadMenuDishListDataFromFirebase(
//                menuId = menuId,
//                onSuccess = { result: List<DishDataFirebase> ->
//                    val dishDataList = result.map { it.toDishData() }
//                    continuation.resume(dishDataList)
//                },
//                onFailure = { e: Exception ->
//                    continuation.resumeWithException(e)
//                }
//            )
//        }
//    }
//
//    override suspend fun downloadMenuSectionListData(
//        menuId: String,
//    ): List<SectionData> {
//        return suspendCancellableCoroutine { continuation ->
//            firestoreDownloadInterface.downloadMenuSectionListDataFromFirebase(
//                menuId = menuId,
//                onSuccess = { result: List<SectionDataFirebase> ->
//                    val sectionData = result.map { it.toSectionData() }
//                    continuation.resume(sectionData)
//                },
//                onFailure = { e: Exception ->
//                    continuation.resumeWithException(e)
//                }
//            )
//        }
//    }
//
//    override suspend fun downloadInfoImageListData(
//        menuId: String,
//    ): List<InfoImageData> {
//        return suspendCancellableCoroutine { continuation ->
//            firestoreDownloadInterface.downloadInfoImageListDataFromFirebase(
//                menuId = menuId,
//                onSuccess = { result: List<InfoImageFirebase> ->
//                    val infoImageDataList = result.map { it.toInfoImageData() }
//                    continuation.resume(infoImageDataList)
//                },
//                onFailure = { e: Exception ->
//                    continuation.resumeWithException(e)
//                }
//            )
//        }
//    }
//}
//
//interface DownloadDataUseCaseInterface {
//    suspend fun downloadMenuIdData(
//        userId: String,
//    ): MenuIdFirebase?
//
//    suspend fun downloadMenuInfoData(
//        menuId: String,
//    ): MenuInfoFirebase?
//
//    suspend fun downloadMenuDishListData(
//        menuId: String,
//    ): List<DishData>
//
//    suspend fun downloadMenuSectionListData(
//        menuId: String,
//    ): List<SectionData>
//
//    suspend fun downloadInfoImageListData(
//        menuId: String,
//    ): List<InfoImageData>
//}