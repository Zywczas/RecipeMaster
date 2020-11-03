package com.zywczas.recipemaster.di

import com.zywczas.recipemaster.views.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuilderModule {

    @ContributesAndroidInjector
    abstract fun contributeMainActivity() : MainActivity

}