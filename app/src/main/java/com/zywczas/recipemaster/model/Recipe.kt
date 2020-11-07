package com.zywczas.recipemaster.model

data class Recipe (
    val title: String,
    val foodDescription: String,
    val ingredientsDescription: String,
    val preparingDescription: String,
    val images: List<String>?
)