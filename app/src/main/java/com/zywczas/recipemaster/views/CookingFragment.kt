package com.zywczas.recipemaster.views

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.RequestManager
import com.google.android.material.snackbar.Snackbar
import com.zywczas.recipemaster.R
import com.zywczas.recipemaster.model.Recipe
import com.zywczas.recipemaster.utilities.*
import com.zywczas.recipemaster.viewmodels.CookingViewModel
import com.zywczas.recipemaster.viewmodels.UniversalViewModelFactory
import kotlinx.android.synthetic.main.fragment_cooking.*
import javax.inject.Inject

class CookingFragment @Inject constructor(
    private val viewModelFactory : UniversalViewModelFactory,
    private val glide : RequestManager
) : Fragment(R.layout.fragment_cooking) {

    private val viewModel : CookingViewModel by viewModels { viewModelFactory }
    private val user by lazyAndroid { requireArguments().let { CookingFragmentArgs.fromBundle(it).user } }
    private val appBarConfig by lazyAndroid { AppBarConfiguration(setOf(R.id.destination_login)) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObserver()
        getRecipeOnViewModelInit()
        val welcomeMsg = "${getString(R.string.logged_as)} $user"
        showSnackbar(welcomeMsg)
        setupOnClickListeners()
        toolbar_cooking.setupWithNavController(findNavController(), appBarConfig)
    }

    private fun setupObserver(){
        viewModel.recipe.observe(viewLifecycleOwner) { resource ->
            showProgressBar(false)
            when (resource.status) {
                Status.SUCCESS -> updateUI(resource.data!!)
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
                .into(food1_imageView)
        }
        recipe.images?.get(1)?.let {
            glide.load(it)
                .into(food2_imageView)
        }
        recipe.images?.get(2)?.let {
            glide.load(it)
                .into(food3_imageView)
        }
    }

    private fun showError(msg: Event<Int>){
        msg.getContentIfNotHandled()?.let { showSnackbar(getString(it)) }
    }

    private fun setupOnClickListeners(){
        //todo zapisywanie obrazkow
    }

    private fun getRecipeOnViewModelInit(){
        showProgressBar(true)
        viewModel.getRecipeOnViewModelInit()
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

}