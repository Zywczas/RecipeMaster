package com.zywczas.recipemaster.views

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.navigation.fragment.findNavController
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.zywczas.recipemaster.utilities.NetworkCheck
import com.zywczas.recipemaster.R
import com.zywczas.recipemaster.utilities.logD
import com.zywczas.recipemaster.utilities.showToast
import kotlinx.android.synthetic.main.fragment_login.*
import javax.inject.Inject

class LoginFragment @Inject constructor(
    private val network: NetworkCheck,
    private val faceLoginManager: LoginManager,
    private val faceCallbackManager : CallbackManager
) : Fragment(R.layout.fragment_login) {

    private var isLoggedIn = false
    private var userName: String? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkIfLoggedInAndGetProfile()
        setupLoginManagerCallback()
        setupUi()
        setupOnClickListeners()
    }

    private fun checkIfLoggedInAndGetProfile(){
        val accessToken = AccessToken.getCurrentAccessToken()
        isLoggedIn = accessToken != null && accessToken.isExpired.not()
        val profile = Profile.getCurrentProfile()
        userName = profile?.name
    }

    private fun setupLoginManagerCallback(){
        faceLoginManager.registerCallback(faceCallbackManager, object : FacebookCallback<LoginResult> {
                override fun onSuccess(result: LoginResult?) {
                    val accessToken = result?.accessToken
                    isLoggedIn = true
                }

                override fun onCancel() {
                    isLoggedIn = false
                    showToast("In order to proceed you need to be logged in") //todo wrzucic w stringi
                }

                override fun onError(error: FacebookException?) {
                    isLoggedIn = false
                    error?.let { logD(it) }
                    showToast("Problem with login to Facebook.")
                }

            })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        faceCallbackManager.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun setupUi(){
        speed_dial_login.inflate(R.menu.menu_speed_dial)
    }

    private fun setupOnClickListeners(){
        speed_dial_login.setOnActionSelectedListener { item ->
            when(item.id){
                R.id.get_recipe_item -> {
                    goToCookingFragmentAfterVerification()
                    true
                }
                R.id.facebook_item -> {
                    loginWithFacebookIfConnected()
                    true
                }
                else -> false
            }
        }
    }

    private fun goToCookingFragmentAfterVerification(){
        if (network.isConnected.not()) {
            showToast(getString(R.string.need_for_network))
        } else {
            if (isLoggedIn.not()){
                showToast(getString(R.string.need_for_login))
            } else {
                goToCookingFragment()
            }
        }
    }

    private fun goToCookingFragment(){
        val name = userName ?: ""
        val directions = LoginFragmentDirections.actionToCooking(name)
        findNavController().navigate(directions)
    }

    private fun loginWithFacebookIfConnected(){
        if (isLoggedIn){
            showToast(getString(R.string.you_are_logged_in))
        } else {
            if (network.isConnected.not()) {
                showToast(getString(R.string.connection_problem))
            } else {
                LoginManager.getInstance().logInWithReadPermissions(this, mutableListOf("email", "public_profile"))
                checkIfLoggedInAndGetProfile()
            }
        }
    }

}