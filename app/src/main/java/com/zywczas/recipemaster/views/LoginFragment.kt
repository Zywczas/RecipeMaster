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
    }

    private fun setupOnClickListeners(){
        fab_main_login.setOnClickListener {
            showToast("dziala")
        }
    }
}