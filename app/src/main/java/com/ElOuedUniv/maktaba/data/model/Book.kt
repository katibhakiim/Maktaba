package com.ElOuedUniv.maktaba.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Book(
    val isbn: String,
    val title: String,
    @SerialName("nb_pages")
    val nbPages: Int,
    @SerialName("image_url")
    val imageUrl: String? = null
)

