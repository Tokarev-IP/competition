package com.example.catalog.content.domain.functions

import android.net.Uri
import com.example.catalog.content.domain.data.DocDishData

interface CreateDocFileInterface {

    suspend fun createMenuDoc(
        folderUri: Uri,
        language: String = "",
        filename: String = "menu_doc_${language}.docx",
        dishList: List<DocDishData>,
    )

}