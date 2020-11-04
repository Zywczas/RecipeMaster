package com.zywczas.recipemaster.utilities

import android.widget.Toast
import androidx.fragment.app.Fragment

fun Fragment.showToast(message: String) =
    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()

fun <T> lazyAndroid(initializer: () -> T) : Lazy<T> = lazy(LazyThreadSafetyMode.NONE, initializer)