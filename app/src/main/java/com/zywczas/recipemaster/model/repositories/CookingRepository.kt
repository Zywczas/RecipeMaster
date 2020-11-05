package com.zywczas.recipemaster.model.repositories

import com.zywczas.recipemaster.model.Recipe
import com.zywczas.recipemaster.model.webservice.RecipeRestApiService
import com.zywczas.recipemaster.utilities.Resource
import com.zywczas.recipemaster.utilities.logD
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.schedulers.Schedulers
import retrofit2.HttpException
import javax.inject.Inject

class CookingRepository @Inject constructor(private val restApi : RecipeRestApiService) {

    private val httpError by lazy { "Problem with downloading recipe. Error code: " }
    private val generalError by lazy { "Problem with downloading recipe. Check connection and try again." }

    fun getRecipe() : Flowable<Resource<Recipe>> {
        val apiSingle = restApi.getRecipe()
        return apiSingle
            .subscribeOn(Schedulers.io())
            .map { Resource.success(it) }
            .onErrorReturn { getError(it) }
            .toFlowable()
    }

    private fun getError(e: Throwable) : Resource<Recipe>{
        return if (e is HttpException) {
            logD(e)
            Resource.error(httpError + e.code(), null)
        } else {
            logD(e)
            Resource.error(generalError, null)
        }
    }


}