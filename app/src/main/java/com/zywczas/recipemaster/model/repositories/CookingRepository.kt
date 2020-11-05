package com.zywczas.recipemaster.model.repositories

import android.app.Application
import com.zywczas.recipemaster.R
import com.zywczas.recipemaster.model.Recipe
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

    fun getRecipe() : Flowable<Resource<Recipe>> {
        val apiSingle = restApi.getRecipe()
        return apiSingle
            .subscribeOn(Schedulers.io())
            .map { recipe ->
                recipe.convertIngredientsListToDescription()
                recipe.convertPreparingStepsToDescription()
                Resource.success(recipe) }
            .onErrorReturn { getError(it) }
            .toFlowable()
    }

    private fun getError(e: Throwable) : Resource<Recipe>{
        return if (e is HttpException) {
            logD(e)
            val httpError = R.string.http_error_recipe
            Resource.error(httpError, null)
        } else {
            logD(e)
            val generalError= R.string.general_restapi_error
            Resource.error(generalError, null)
        }
    }


}