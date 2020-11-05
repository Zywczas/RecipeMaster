package com.zywczas.recipemaster.di

import androidx.lifecycle.ViewModel
import com.zywczas.recipemaster.viewmodels.CookingViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelFactoryModule {

    @Binds
    @IntoMap
    @ViewModelKey(CookingViewModel::class)
    abstract fun bindCookingVM(vm: CookingViewModel) : ViewModel


}