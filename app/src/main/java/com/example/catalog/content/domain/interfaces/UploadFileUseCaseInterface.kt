package com.example.catalog.content.domain.interfaces

interface UploadFileUseCaseInterface {

    suspend fun uploadMenuPictureUsingByteArray(
        pathString: String = "pic",
        menuId: String,
        byteArray: ByteArray,
    )

    suspend fun uploadDishPictureUsingByteArray(
        pathString: String = "dish",
        menuId: String,
        dishId: String,
        byteArray: ByteArray,
    )
}