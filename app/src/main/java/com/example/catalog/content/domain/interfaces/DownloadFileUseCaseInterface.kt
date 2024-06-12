package com.example.catalog.content.domain.interfaces

import android.net.Uri

interface DownloadFileUseCaseInterface {

    fun downloadUriOfMenuPicture(
        pathString: String = "pic",
        menuId: String,
        onSuccess: (uri: Uri) -> Unit,
        onFailure: (msg: String) -> Unit,
    )

    fun downloadUriOfDishPicture(
        pathString: String = "dish",
        menuId: String,
        dishId: String,
        onSuccess: (uri: Uri) -> Unit,
        onFailure: (msg: String) -> Unit,
    )
}