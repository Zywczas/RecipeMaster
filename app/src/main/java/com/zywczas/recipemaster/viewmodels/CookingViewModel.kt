package com.zywczas.recipemaster.viewmodels

import androidx.lifecycle.ViewModel
import com.zywczas.recipemaster.NetworkCheck
import com.zywczas.recipemaster.model.repositories.CookingRepository
import javax.inject.Inject

class CookingViewModel @Inject constructor(
    network : NetworkCheck,
    repo: CookingRepository
) : ViewModel() {


}