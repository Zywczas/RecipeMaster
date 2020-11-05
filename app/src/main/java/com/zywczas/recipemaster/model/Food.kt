package com.zywczas.recipemaster.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


data class Food(
    @SerializedName("title")
    @Expose
    val title: String?,

    @SerializedName("description")
    @Expose
    val description: String?,

    @SerializedName("ingredients")
    @Expose
    val ingredients: List<String>?,

    @SerializedName("preparing")
    @Expose
    val preparing: List<String>?,

    @SerializedName("imgs")
    @Expose
    val images: List<String>?,
)

