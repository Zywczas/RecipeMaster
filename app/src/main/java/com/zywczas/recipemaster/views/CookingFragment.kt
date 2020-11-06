package com.zywczas.recipemaster.views

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.android.material.snackbar.Snackbar
import com.zywczas.recipemaster.R
import com.zywczas.recipemaster.model.Recipe
import com.zywczas.recipemaster.utilities.*
import com.zywczas.recipemaster.viewmodels.CookingViewModel
import com.zywczas.recipemaster.viewmodels.UniversalViewModelFactory
import kotlinx.android.synthetic.main.fragment_cooking.*
import java.io.File
import java.io.FileOutputStream
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class CookingFragment @Inject constructor(
    private val viewModelFactory : UniversalViewModelFactory,
    private val glide : RequestManager
) : Fragment(R.layout.fragment_cooking), SaveImageDialog.SaveImageListener {

    private val viewModel : CookingViewModel by viewModels { viewModelFactory }
    private val userName by lazyAndroid { requireArguments().let { CookingFragmentArgs.fromBundle(it).userName } }
    private val appBarConfig by lazyAndroid { AppBarConfiguration(setOf(R.id.destination_login)) }
    private var recipe: Recipe? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObserver()
        getRecipeOnViewModelInit()
        val welcomeMsg = "${getString(R.string.logged_as)} $userName"
        showSnackbar(welcomeMsg)
        setupOnClickListeners()
        toolbar_cooking.setupWithNavController(findNavController(), appBarConfig)
    }

    private fun setupObserver(){
        viewModel.recipe.observe(viewLifecycleOwner) { resource ->
            showProgressBar(false)
            when (resource.status) {
                Status.SUCCESS -> {
                    recipe = resource.data!!
                    updateUI(resource.data)
                }
                Status.ERROR -> showError(resource.message!!)
            }
        }
    }

    private fun showProgressBar(visible : Boolean){
        progressBar_cooking.isVisible = visible
    }

    @SuppressLint("SetTextI18n")
    private fun updateUI(recipe: Recipe){
        recipe.title?.let {
            toolbar_cooking.title = "$it ${getString(R.string.recipe)}"
            food_name_textView.text = "$it:" }
        food_description_textView.text = recipe.description
        ingredients_list_textView.text = recipe.ingredientsDescription
        preparing_steps_textView.text = recipe.preparingDescription
        recipe.images?.get(0)?.let {
            glide.load(it)
                .into(food0_imageView)
        }
        recipe.images?.get(1)?.let {
            glide.load(it)
                .into(food1_imageView)
        }
        recipe.images?.get(2)?.let {
            glide.load(it)
                .into(food2_imageView)
        }
    }

    private fun showError(msg: Event<Int>){
        msg.getContentIfNotHandled()?.let { showSnackbar(getString(it)) }
    }

    private fun showSnackbar(msg: String){
        val color = ContextCompat.getColor(requireContext(), R.color.darkGrey)
        val snackbar = Snackbar.make(requireView(), msg, Snackbar.LENGTH_LONG)
            .setBackgroundTint(color)
        val view = snackbar.view
        val textView = view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
        textView.textAlignment = View.TEXT_ALIGNMENT_CENTER
        snackbar.show()
    }

    private fun getRecipeOnViewModelInit(){
        showProgressBar(true)
        viewModel.getRecipeOnViewModelInit()
    }

    private fun setupOnClickListeners(){
        food0_imageView.setOnClickListener(imageClickListener)
        food1_imageView.setOnClickListener(imageClickListener)
        food2_imageView.setOnClickListener(imageClickListener)
    }

    private val imageClickListener = View.OnClickListener {
        val viewIndex = it.tag as Int
        areYouSureDialog(viewIndex)
    }

    private fun areYouSureDialog(viewIndex : Int){
        val dialog = SaveImageDialog()
        val bundle = Bundle()
        bundle.putInt(EXTRA_VIEW_INDEX, viewIndex)
        dialog.arguments = bundle
        dialog.show(childFragmentManager, "SaveImage")
    }

    override fun saveImage(index: Int) {
        if (recipe?.images?.get(index) == null){
            showSnackbar(getString(R.string.no_image_to_save))
        } else {
            val url = recipe!!.images!![index]
            saveImageToExternalStorage(url)
        }
    }
//todo jak juz bedzie dzialac to sprobowac wrzucic to w dialog
    private fun saveImageToExternalStorage(url: String){
        val dirPath = Environment.getExternalStorageDirectory().absolutePath + "/" //todo poprawic...
        val dir = File(dirPath)
        val timeStamp = SimpleDateFormat("yyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val fileName = timeStamp + "_" + url.substring(url.lastIndexOf("/") + 1)
        glide.load(url).into(object : CustomTarget<Drawable>(){
            override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                showProgressBar(true)
                val bitmap = resource.toBitmap()
                saveToStorage(bitmap, dir, fileName)

//                val resolver = context?.applicationContext?.contentResolver
//
//                if (resolver == null) {
//                    //todo
//                    showToast("nie ma contextu...")
//                    logD("nie ma contextu...")
//                } else {
//                    val imageCollection = MediaStore.Images.Media.getContentUri(
//                        MediaStore.VOLUME_EXTERNAL
//                    )
//                }
            }

            override fun onLoadCleared(placeholder: Drawable?) {
            }

            override fun onLoadFailed(errorDrawable: Drawable?) {
                showSnackbar(getString(R.string.image_saving_error))
            }
        })
    }

    private fun saveToStorage(image: Bitmap, storageDir: File, fileName: String){
        val imageFile = File(storageDir, fileName)
        val savedImagePath = imageFile.absolutePath
        try {
            val out = FileOutputStream(imageFile)
            image.compress(Bitmap.CompressFormat.JPEG, 100, out)
            out.close()
            //todo dac tutaj kolejny dialog - Success
            showSnackbar(getString(R.string.image_saved))
            showProgressBar(false)
        } catch (e: Exception){
            showSnackbar(getString(R.string.image_saving_error))
            logD(e)
        }

    }





















}