package com.zywczas.recipemaster.model

import com.zywczas.recipemaster.model.webservice.RecipeFromApi

fun toRecipe(recipeFromApi: RecipeFromApi) : Recipe {
    recipeFromApi.convertIngredientsListToDescription()
    recipeFromApi.convertPreparingStepsToDescription()
    return Recipe(
        recipeFromApi.title ?: "",
        recipeFromApi.foodDescription ?: "",
        recipeFromApi.ingredientsDescription,
        recipeFromApi.preparingDescription,
        recipeFromApi.images
    )
}
