package com.zywczas.recipemaster

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import com.facebook.AccessToken
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionManager @Inject constructor(
    private val app: Application
) {

    var isConnected = false
        private set
    var isLoggedIn = false
        get() = if (field) {field} else {
            val token = AccessToken.getCurrentAccessToken()
            field = token != null && token.isExpired.not()
            field
        }

    init {
        registerNetworkCallback()
    }

    private fun registerNetworkCallback() {
        val cm =
            app.applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        cm.registerDefaultNetworkCallback(object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                isConnected = true
            }

            override fun onLost(network: Network) {
                super.onLost(network)
                isConnected = false
            }
        })
    }

}