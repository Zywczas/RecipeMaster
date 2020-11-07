package com.zywczas.recipemaster.utilities

import android.util.Log
import android.widget.Toast
import androidx.fragment.app.Fragment

fun Fragment.showToast(message: String) =
    Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()

fun <T> lazyAndroid(initializer: () -> T) : Lazy<T> = lazy(LazyThreadSafetyMode.NONE, initializer)

private const val tag = "RecipeMaster"
fun logD(msg : String) = Log.d(tag, msg)
fun logD(e : Throwable) = Log.d(tag, "${e.message}")