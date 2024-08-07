package com.example.catalog.content.domain.data

import com.google.firebase.firestore.PropertyName
import kotlinx.serialization.Serializable

@Serializable
data class MenuIdFirebase @JvmOverloads constructor(
    @get:PropertyName("id")
    @set:PropertyName("id")
    var id: String = ""
)

@Serializable
data class DishDataFirebase @JvmOverloads constructor(
    @get:PropertyName("id")
    @set:PropertyName("id")
    var id: String = "",

    @get:PropertyName("name")
    @set:PropertyName("name")
    var name: String = "",

    @get:PropertyName("description")
    @set:PropertyName("description")
    var description: String = "",

    @get:PropertyName("price")
    @set:PropertyName("price")
    var price: Double = 0.0,

    @get:PropertyName("weight")
    @set:PropertyName("weight")
    var weight: Double = 0.0,

    @get:PropertyName("image")
    @set:PropertyName("image")
    var image: String? = null,
)

@Serializable
data class MenuInfoFirebase @JvmOverloads constructor(
    @get:PropertyName("id")
    @set:PropertyName("id")
    var id: String = "",

    @get:PropertyName("name")
    @set:PropertyName("name")
    var name: String = "",
)
