package com.zywczas.recipemaster.views

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.ContentValues
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
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
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class CookingFragment @Inject constructor(
    private val viewModelFactory: UniversalViewModelFactory,
    private val glide: RequestManager
) : Fragment(R.layout.fragment_cooking) {

    private val viewModel: CookingViewModel by viewModels { viewModelFactory }
    private val userName by lazyAndroid { requireArguments().let { CookingFragmentArgs.fromBundle(it).userName } }
    private val appBarConfig by lazyAndroid { AppBarConfiguration(setOf(R.id.destination_login)) }
    private var recipe: Recipe? = null
    private var arePermissionsGranted = false
    private val storageRequestCode by lazyAndroid { 777 }
    private val permissions by lazyAndroid {
        arrayOf(
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
    }
    private var requestedImageUrl: String? = null
    private val photoUrlKey by lazyAndroid { "photo key" }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        verifyStoragePermissions()
        requestedImageUrl = savedInstanceState?.getString(photoUrlKey)
    }

    private fun verifyStoragePermissions() {
        if (ContextCompat.checkSelfPermission(requireContext(),permissions[0]) == PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(requireContext(),permissions[1]) == PackageManager.PERMISSION_GRANTED
        ) {
            arePermissionsGranted = true
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObserver()
        getRecipeOnViewModelInit()
        val welcomeMsg = "${getString(R.string.logged_as)} $userName"
        showSnackbar(welcomeMsg)
        setupOnClickListeners()
        toolbar_cooking.setupWithNavController(findNavController(), appBarConfig)
    }

    private fun setupObserver() {
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

    private fun showProgressBar(visible: Boolean) {
        progressBar_cooking.isVisible = visible
    }

    private fun updateUI(recipe: Recipe) {
        recipe.title.let {
            toolbar_cooking.title = "$it ${getString(R.string.recipe)}"
            @SuppressLint("SetTextI18n")
            foodName_textView_cooking.text = "$it:"
        }
        foodDescription_textView_cooking.text = recipe.foodDescription
        ingredientsList_textView_cooking.text = recipe.ingredientsDescription
        preparingSteps_textView_cooking.text = recipe.preparingDescription
        recipe.images?.get(0)?.let {
            glide.load(it)
                .into(food0_imageView_cooking)
        }
        recipe.images?.get(1)?.let {
            glide.load(it)
                .into(food1_imageView_cooking)
        }
        recipe.images?.get(2)?.let {
            glide.load(it)
                .into(food2_imageView_cooking)
        }
    }

    private fun showError(msg: Event<Int>) {
        msg.getContentIfNotHandled()?.let { showSnackbar(getString(it)) }
    }

    private fun showSnackbar(msg: String) {
        val color = ContextCompat.getColor(requireContext(), R.color.darkGrey)
        val snackbar = Snackbar.make(requireView(), msg, Snackbar.LENGTH_LONG)
            .setBackgroundTint(color)
        val view = snackbar.view
        val textView = view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
        textView.textAlignment = View.TEXT_ALIGNMENT_CENTER
        snackbar.show()
    }

    private fun getRecipeOnViewModelInit() {
        showProgressBar(true)
        viewModel.getRecipeOnViewModelInit()
    }

    private fun setupOnClickListeners() {
        food0_imageView_cooking.setOnClickListener(imageClickListener)
        food1_imageView_cooking.setOnClickListener(imageClickListener)
        food2_imageView_cooking.setOnClickListener(imageClickListener)
    }

    private val imageClickListener = View.OnClickListener {
        val index = getIndex(it)
        if (recipe?.images?.get(index) == null) {
            showSnackbar(getString(R.string.no_image_to_save))
        } else {
            requestedImageUrl = recipe!!.images!![index]
            showSaveConfirmationDialog()
        }
    }

    private fun getIndex(view: View): Int =
        when (view.id) {
            food0_imageView_cooking.id -> 0
            food1_imageView_cooking.id -> 1
            food2_imageView_cooking.id -> 2
            else -> 666
        }

    private fun showSaveConfirmationDialog() {
        val builder = AlertDialog.Builder(requireContext()).create()
        val dialogView = layoutInflater.inflate(R.layout.dialog_save_image, null)
        val yes = dialogView.findViewById<TextView>(R.id.yes_textView_dialog)
        val no = dialogView.findViewById<TextView>(R.id.no_textView_dialog)
        yes.setOnClickListener {
            checkPermissionsAndSaveImage()
            builder.dismiss()
        }
        no.setOnClickListener { builder.dismiss() }
        builder.setView(dialogView)
        builder.show()
    }

    private fun checkPermissionsAndSaveImage() {
        if (arePermissionsGranted) {
            downloadAndSaveImage()
        } else {
            askForPermissionsAndSavePhoto()
        }
    }

    private fun downloadAndSaveImage() {
        val timeStamp = SimpleDateFormat("yyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val urlLastPart = requestedImageUrl!!.substring(requestedImageUrl!!.lastIndexOf("/") + 1)
        val imageName = "${timeStamp}_$urlLastPart"
        glide.load(requestedImageUrl)
            .into(object : CustomTarget<Drawable>() {
                override fun onResourceReady(resource: Drawable,transition: Transition<in Drawable>?) {
                    val bitmap = resource.toBitmap()
                    saveImageToGallery(bitmap, imageName)
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                }

                override fun onLoadFailed(errorDrawable: Drawable?) {
                    showSnackbar(getString(R.string.image_saving_error))
                }
            })
    }

    private fun saveImageToGallery(bitmap: Bitmap, imageName: String) {
        var fos: OutputStream? = null
        try {
            val resolver = requireContext().contentResolver
            val contentValues = getContentValues(imageName)
            val imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
            fos = resolver.openOutputStream(imageUri!!)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)
            fos!!.close()
            showInfoDialog(getString(R.string.image_saved))
        } catch (e: Exception){
            fos?.close()
            logD(e)
            showInfoDialog(getString(R.string.image_saving_error))
        }
    }

    private fun getContentValues(imageName: String) : ContentValues {
        return ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, imageName)
            put(MediaStore.MediaColumns.TITLE, "Recipe Master")
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg")
        }
    }

    private fun showInfoDialog(msg : String){
        val builder = AlertDialog.Builder(requireContext()).create()
        val dialogView = layoutInflater.inflate(R.layout.dialog_message, null)
        val textView = dialogView.findViewById<TextView>(R.id.info_txtView_infoDialog)
        textView.text = msg
        val ok = dialogView.findViewById<ImageView>(R.id.confirm_imageView_infoDialog)
        ok.setOnClickListener { builder.dismiss() }
        builder.setView(dialogView)
        builder.show()
    }

    private fun askForPermissionsAndSavePhoto() {
        requestPermissions(permissions, storageRequestCode)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            storageRequestCode -> {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    arePermissionsGranted = true
                    downloadAndSaveImage()
                } else {
                    showToast(getString(R.string.permission_warning))
                }
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        requestedImageUrl?.let { outState.putString(photoUrlKey, it) }
    }


}