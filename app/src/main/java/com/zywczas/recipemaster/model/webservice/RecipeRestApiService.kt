package com.zywczas.recipemaster.model.webservice

import com.zywczas.recipemaster.model.Recipe
import io.reactivex.rxjava3.core.Single
import retrofit2.HttpException
import retrofit2.Response
import retrofit2.http.GET

/**
 * Interface for contacting with https://moodup.team/
 */
interface RecipeRestApiService {

    @Throws(HttpException::class)
    @GET("test/info.php")
    fun getRecipe() : Single<Recipe>

}