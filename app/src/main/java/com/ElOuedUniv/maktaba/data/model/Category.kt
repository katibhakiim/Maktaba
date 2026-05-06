package com.ElOuedUniv.maktaba.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Category(
    val id: String,
    val name: String,
    val description: String,
    @SerialName("icon_res")
    val iconRes: Int = android.R.drawable.ic_menu_compass
)
