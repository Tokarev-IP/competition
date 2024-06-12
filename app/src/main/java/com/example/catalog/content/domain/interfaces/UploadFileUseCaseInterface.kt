package com.example.catalog.content.domain.interfaces

interface UploadFileUseCaseInterface {

    fun uploadMenuPictureUsingByteArray(
        pathString: String = "pic",
        menuId: String,
        byteArray: ByteArray,
        onSuccess: () -> Unit,
        onFailure: (msg: String) -> Unit
    )

    fun uploadDishPictureUsingByteArray(
        pathString: String = "dish",
        menuId: String,
        dishId: String,
        byteArray: ByteArray,
        onSuccess: () -> Unit,
        onFailure: (msg: String) -> Unit
    )
}