package com.zywczas.recipemaster.model.repositories

import com.zywczas.recipemaster.R
import com.zywczas.recipemaster.model.Recipe
import com.zywczas.recipemaster.model.toRecipe
import com.zywczas.recipemaster.model.webservice.RecipeFromApi
import com.zywczas.recipemaster.model.webservice.RecipeRestApiService
import com.zywczas.recipemaster.utilities.Resource
import com.zywczas.recipemaster.utilities.logD
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.schedulers.Schedulers
import retrofit2.HttpException
import javax.inject.Inject

class CookingRepository @Inject constructor(
    private val restApi : RecipeRestApiService
) {

    fun getRecipeFromApi() : Flowable<Resource<Recipe>> {
        val apiSingle = restApi.getRecipe()
        return apiSingle
            .subscribeOn(Schedulers.io())
            .map { recipeFromApi ->
                Resource.success(toRecipe(recipeFromApi)) }
            .onErrorReturn { getError(it) }
            .toFlowable()
    }

    private fun getError(e: Throwable) : Resource<Recipe>{
        val generalError= R.string.general_restapi_error
        if (e is HttpException) {
            logD("HttpException: ${e.code()}")
        }
        logD(e)
        return Resource.error(generalError, null)
    }


}