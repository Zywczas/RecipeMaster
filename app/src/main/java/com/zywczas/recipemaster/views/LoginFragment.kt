package com.zywczas.recipemaster.views

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import com.zywczas.recipemaster.R
import com.zywczas.recipemaster.utilities.showToast
import kotlinx.android.synthetic.main.fragment_login.*
import javax.inject.Inject


class LoginFragment @Inject constructor() : Fragment(R.layout.fragment_login) {


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupOnClickListeners()
        setupUi()

    }

    private fun setupUi(){
        speed_dial_login.inflate(R.menu.menu_speed_dial)
    }

    private fun setupOnClickListeners(){
        speed_dial_login.setOnActionSelectedListener { item ->
            when(item.id){
                R.id.get_recipe_item -> {
                    showToast("przepisy dzialaja")
                    true
                }
                R.id.facebook_item -> {
                    showToast("fejs dziala, yoohu!!")
                    true
                }
                else -> false
            }
        }

    }
}