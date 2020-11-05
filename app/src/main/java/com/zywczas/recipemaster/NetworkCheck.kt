package com.zywczas.recipemaster

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NetworkCheck @Inject constructor(private val app: Application) {
//todo sprawdzic
//    var isConnected = false
//        private set

    private val connectivityManager =
        app.applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    val isConnected = connectivityManager.isDefaultNetworkActive

//    init {
//        registerNetworkCallback()
//    }
//
//    private fun registerNetworkCallback() {
//        connectivityManager.registerDefaultNetworkCallback(object : ConnectivityManager.NetworkCallback(){
//
//        })
//    }

}