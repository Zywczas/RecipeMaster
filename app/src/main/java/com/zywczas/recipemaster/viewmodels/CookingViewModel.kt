package com.zywczas.recipemaster.viewmodels

import android.app.Application
import android.content.ContentValues
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.lifecycle.LiveData
import androidx.lifecycle.LiveDataReactiveStreams
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.bumptech.glide.RequestManager
import com.zywczas.recipemaster.NetworkCheck
import com.zywczas.recipemaster.R
import com.zywczas.recipemaster.model.Recipe
import com.zywczas.recipemaster.model.repositories.CookingRepository
import com.zywczas.recipemaster.utilities.Resource
import java.io.OutputStream
import javax.inject.Inject

class CookingViewModel @Inject constructor(
    private val repo : CookingRepository,
    private val app: Application,
    private val glide : RequestManager
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
        val source = LiveDataReactiveStreams.fromPublisher(repo.getRecipe())
        _recipe.addSource(source) {
            _recipe.postValue(it)
            _recipe.removeSource(source)
        }
    }

//    private fun saveImageToGallery(bitmap: Bitmap, fileName: String) {
//        var fos: OutputStream? = null
//        app.applicationContext.contentResolver?.also { resolver ->
//            val contentValues = ContentValues().apply {
//                put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
//                put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg")
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
//                    put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
//                    put(MediaStore.MediaColumns.IS_PENDING, 1)
//                }
//            }
//            val imageUri: Uri? = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
//            fos = imageUri?.let { resolver.openOutputStream(it) }
//        }
//        fos?.use { bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it) }
//        showSnackbar(getString(R.string.image_saved))
//    }


}