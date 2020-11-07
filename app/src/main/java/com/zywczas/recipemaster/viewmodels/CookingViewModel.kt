package com.zywczas.recipemaster.viewmodels

import android.app.Application
import android.provider.MediaStore
import androidx.lifecycle.LiveData
import androidx.lifecycle.LiveDataReactiveStreams
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.bumptech.glide.RequestManager
import com.zywczas.recipemaster.model.Recipe
import com.zywczas.recipemaster.model.repositories.CookingRepository
import com.zywczas.recipemaster.utilities.Resource
import javax.inject.Inject

class CookingViewModel @Inject constructor(
    private val repo : CookingRepository
) : ViewModel() {

    private var isFirstRecipeRequested = false
    private val _recipe by lazy { MediatorLiveData<Resource<Recipe>>() }
    val recipe : LiveData<Resource<Recipe>> by lazy { _recipe }

    fun getRecipeOnViewModelInit(){
        if (isFirstRecipeRequested.not()){
            isFirstRecipeRequested = true
            downloadRecipe()
        }
    }

    private fun downloadRecipe(){
        val source = LiveDataReactiveStreams.fromPublisher(repo.getRecipeFromApi())
        _recipe.addSource(source) {
            _recipe.postValue(it)
            _recipe.removeSource(source)
        }
    }

}