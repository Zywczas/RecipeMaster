package com.zywczas.recipemaster.views

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.zywczas.recipemaster.R
import com.zywczas.recipemaster.utilities.logD
import com.zywczas.recipemaster.utilities.showToast
import kotlinx.android.synthetic.main.fragment_login.*
import javax.inject.Inject


class LoginFragment @Inject constructor() : Fragment(R.layout.fragment_login) {

    private lateinit var faceCallbackManager : CallbackManager
    private var isLoggedIn = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        faceCallbackManager = CallbackManager.Factory.create()
        //todo niby tak tez mozna sprawdzic czy zalogowany
        val accessToken = AccessToken.getCurrentAccessToken()
        val czyZalogowany = accessToken != null && accessToken.isExpired.not()
        logD("zalogowany?: $czyZalogowany")
        setupLoginManagerCallback()

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupOnClickListeners()
        setupUi()
    }

    private fun setupUi(){
        speed_dial_login.inflate(R.menu.menu_speed_dial)
    }

    private fun setupOnClickListeners(){
        speed_dial_login.setOnActionSelectedListener { item ->
            when(item.id){
                R.id.get_recipe_item -> {

                    true
                }
                R.id.facebook_item -> {
                    loginWithFacebook()
                    true
                }
                else -> false
            }
        }
    }

    private fun loginWithFacebook(){
        LoginManager.getInstance().logInWithReadPermissions(this, mutableListOf("email", "public_profile"))
    }

//todo You don't need a registerCallback for login to succeed, you can choose to follow current access token changes with the AccessTokenTracker class described below.
    private fun setupLoginManagerCallback(){
        //todo wrzucic w daggera
    //todo to powinno byc w onCreate jezeli wogole potrzebne

        LoginManager.getInstance().registerCallback(
            faceCallbackManager,
            object : FacebookCallback<LoginResult> {
                override fun onSuccess(result: LoginResult?) {
                    val accessToken = result?.accessToken
                    logD("zarejestrowalo za pomoca fejsa, token: $accessToken")
                    isLoggedIn = true
                    showToast("zalogowano")
                }

                override fun onCancel() {
                    isLoggedIn = false
                    logD("anulowano rejestracje")
                    showToast("In order to proceed you need to be logged in") //todo wrzucic w stringi
                }

                override fun onError(error: FacebookException?) {
                    isLoggedIn = false
                    if (error != null) {
                        logD(error)
                    }
                    showToast("exception")
                }

            })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        faceCallbackManager.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }

}