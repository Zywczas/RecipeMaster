package com.zywczas.recipemaster.di

import android.app.Application
import com.zywczas.recipemaster.BaseApplication
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import javax.inject.Singleton

@Singleton
@Component(modules = [
    AndroidInjectionModule::class,
    AppModule::class,
    ActivityBuilderModule::class,
    ViewModelFactoryModule::class,
    FragmentFactoryModule::class
])
interface AppComponent : AndroidInjector<BaseApplication> {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance app: Application) : AppComponent
    }

}