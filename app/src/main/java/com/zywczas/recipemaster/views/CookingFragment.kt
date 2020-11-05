package com.zywczas.recipemaster.views

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.zywczas.recipemaster.R
import com.zywczas.recipemaster.utilities.lazyAndroid
import com.zywczas.recipemaster.utilities.logD
import com.zywczas.recipemaster.utilities.showToast
import javax.inject.Inject

class CookingFragment @Inject constructor() : Fragment(R.layout.fragment_cooking) {

    private val user by lazyAndroid { requireArguments().let { CookingFragmentArgs.fromBundle(it).user } }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        logD("zalogowany: $user")
        //todo dac snackbar na dole

    }

}