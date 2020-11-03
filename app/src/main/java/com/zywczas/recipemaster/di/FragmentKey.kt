package com.zywczas.recipemaster.di

import androidx.fragment.app.Fragment
import dagger.MapKey
import kotlin.reflect.KClass

@Target(AnnotationTarget.PROPERTY, AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@MapKey
annotation class FragmentKey (val value: KClass<out Fragment>)