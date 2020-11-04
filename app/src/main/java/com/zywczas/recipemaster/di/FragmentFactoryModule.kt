package com.zywczas.recipemaster.di

import androidx.fragment.app.Fragment
import com.zywczas.recipemaster.views.CookingFragment
import com.zywczas.recipemaster.views.LoginFragment
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class FragmentFactoryModule {

    @Binds
    @IntoMap
    @FragmentKey(LoginFragment::class)
    abstract fun bindLoginFragment(fragment: LoginFragment) : Fragment

    @Binds
    @IntoMap
    @FragmentKey(CookingFragment::class)
    abstract fun bindCookingFragment(fragment: CookingFragment) : Fragment

}