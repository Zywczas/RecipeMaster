package com.zywczas.recipemaster.model.webservice

import com.zywczas.recipemaster.model.Food
import io.reactivex.rxjava3.core.Single
import retrofit2.Response
import retrofit2.http.GET

/**
 * Interface for contacting with https://moodup.team/
 */
interface RecipeRestApiService {

    @GET("test/info.php")
    fun getRecipe() : Single<Response<Food>>

}