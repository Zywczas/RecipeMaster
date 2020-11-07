package com.zywczas.recipemaster.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.zywczas.recipemaster.utilities.logD


data class Recipe(
    @SerializedName("title")
    @Expose
    val title: String?,

    @SerializedName("description")
    @Expose
    val foodDescription: String?,

    @SerializedName("ingredients")
    @Expose
    val ingredientsList: List<String>?,

    @SerializedName("preparing")
    @Expose
    val preparingSteps: List<String>?,

    @SerializedName("imgs")
    @Expose
    val images: List<String>?
) {

    lateinit var ingredientsDescription : String
        private set
    lateinit var preparingDescription : String
        private set

    @Suppress("LiftReturnOrAssignment")
    fun convertIngredientsListToDescription(){
        ingredientsDescription = ""
        if (ingredientsList != null){
            val size = ingredientsList.size
            var i = 1
            ingredientsList.forEach {
                val isItLastItem = i == size
                if (isItLastItem){
                    ingredientsDescription += "- $it"
                } else {
                    ingredientsDescription += "- $it\n"
                }
                i++
            }
        }
    }

    @Suppress("LiftReturnOrAssignment")
    fun convertPreparingStepsToDescription(){
        if (preparingSteps != null){
            preparingDescription = ""
            val size = preparingSteps.size
            var i = 1
            preparingSteps.forEach {
                val isItLastItem = i == size
                if (isItLastItem){
                    preparingDescription += "$i. $it"
                } else {
                    preparingDescription += "$i. $it\n\n"
                }
                i++
            }
        }
    }


}

