package com.zywczas.recipemaster.views

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import androidx.navigation.fragment.findNavController
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.zywczas.recipemaster.R
import com.zywczas.recipemaster.utilities.logD
import com.zywczas.recipemaster.utilities.showToast
import kotlinx.android.synthetic.main.fragment_login.*
import javax.inject.Inject

//todo wrzucic co sie da w view model
class LoginFragment @Inject constructor() : Fragment(R.layout.fragment_login) {

    private lateinit var faceCallbackManager : CallbackManager //todo wrzucic w daggera
    private var isLoggedIn = false
    private lateinit var profileTracker: ProfileTracker //todo chyba usunac
    private var profile: Profile? = null
    private var name: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        faceCallbackManager = CallbackManager.Factory.create()
        val accessToken = AccessToken.getCurrentAccessToken()
        isLoggedIn = accessToken != null && accessToken.isExpired.not()
        logD("zalogowany na start?: $isLoggedIn")
        setupLoginManagerCallback()
        //profile tracker sciezka
//        FacebookSdk.sdkInitialize(this.getApplicationContext()) - to kiedys bylo potrzebne, teraz deprecated,
//        mowi ze samo sie wlacza przy aplikacji, np po to zeby byl czlowiek od razu zalogowany
        //todo wrzucic w odpowiednie miejsce i przeanalizowac inicjalizacje, przy automatycznym zalogowaniu i jak po raz pierwszy
        profile = Profile.getCurrentProfile()
        name = profile?.name
        logD("cale imie: $name")
    }

    private fun setupLoginManagerCallback(){
        //todo wrzucic w daggera
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
                    error?.let { logD(it) }
                    showToast("Problem with login to Facebook.")
                }

            })
    }

    //to jest potrzebne fejsbookowi, callback manager podaje dane do LoginManager
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        faceCallbackManager.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUi()
        setupOnClickListeners()
    }

    private fun setupUi(){
        speed_dial_login.inflate(R.menu.menu_speed_dial)
    }

    private fun setupOnClickListeners(){
        speed_dial_login.setOnActionSelectedListener { item ->
            when(item.id){
                R.id.get_recipe_item -> {
                    goToCookingFragment()
                    true
                }
                R.id.facebook_item -> {
                    loginWithFacebook() //todo dodac ifNotLoggedIn i sprawdzenie internetu,
                    true
                }
                else -> false
            }
        }
    }

    private fun goToCookingFragment(){
        //todo dac sprawdzenie czy jest internet i czy zalogowany i czy jest profil
        //chwilowe rozwiazanie
        val imie = name ?: "imie nie zaladowane"
        val directions = LoginFragmentDirections.actionToCooking(imie)
        findNavController().navigate(directions)
    }

    private fun loginWithFacebook(){
        LoginManager.getInstance().logInWithReadPermissions(this, mutableListOf("email", "public_profile"))
    }

}