package com.zywczas.recipemaster.viewmodels

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.LiveDataReactiveStreams
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.bumptech.glide.RequestManager
import com.zywczas.recipemaster.model.Recipe
import com.zywczas.recipemaster.model.webservice.RecipeFromApi
import com.zywczas.recipemaster.model.repositories.CookingRepository
import com.zywczas.recipemaster.utilities.Resource
import javax.inject.Inject

class CookingViewModel @Inject constructor(
    private val repo : CookingRepository,
    private val app: Application,
    private val glide : RequestManager
) : ViewModel() {

    private var isFirstRecipeRequested = false
    private val _recipe by lazy { MediatorLiveData<Resource<Recipe>>() }
    val recipeFromApiFromApi : LiveData<Resource<Recipe>> by lazy { _recipe }

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

//    private fun downloadAndSaveImage() {
//        val timeStamp = SimpleDateFormat("yyMMdd_HHmmss", Locale.getDefault()).format(Date())
//        val photoName = requestedImageUrl!!.substring(requestedImageUrl!!.lastIndexOf("/") + 1)
//        val fileName = "${timeStamp}_$photoName"
//        glide.load(requestedImageUrl)
//            .into(object : CustomTarget<Drawable>() {
//                override fun onResourceReady(
//                    resource: Drawable,
//                    transition: Transition<in Drawable>?
//                ) {
//                    val bitmap = resource.toBitmap()
//                    saveImageToGallery(bitmap, fileName)
//                }
//
//                override fun onLoadCleared(placeholder: Drawable?) {
//                }
//
//                override fun onLoadFailed(errorDrawable: Drawable?) {
//                    showSnackbar(getString(R.string.image_saving_error))
//                }
//            })
//    }

//    private fun saveImageToGallery(bitmap: Bitmap, fileName: String) {
//        var fos: OutputStream? = null
//            val contentValues = ContentValues().apply {
//                put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
//                put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg")
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//                    put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
//                    put(MediaStore.MediaColumns.IS_PENDING, 1)
//                }
//            }
//            val imageUri: Uri? =
//                app.applicationContext.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
//            fos = imageUri?.let { app.applicationContext.contentResolver.openOutputStream(it) }
//
//        fos?.use { bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it) }
//        showInfoDialog(getString(R.string.image_saved))
//    }


}