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
    private val faceCallbackManager : CallbackManager,
    private val faceAccessToken : AccessToken?
) : Fragment(R.layout.fragment_login) {

    private var isLoggedIn = false
    private var userName: String? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        logD("access token na start: $faceAccessToken ")
        checkIfLoggedInAndGetProfile()
        setupLoginManagerCallback()
        setupUi()
        setupOnClickListeners()
    }

    private fun checkIfLoggedInAndGetProfile(){
        checkIfLoggedIn { complete ->
            if (complete && isLoggedIn) {
                getFacebookProfile()
            }
        }
    }

    private fun checkIfLoggedIn(complete : (Boolean) -> Unit){
        isLoggedIn = faceAccessToken != null && faceAccessToken.isExpired.not()
        complete(true)
    }

    private fun getFacebookProfile(){
        val profile = Profile.getCurrentProfile()
        logD("profile: $profile i ${profile.firstName}")
        logD("access token: $faceAccessToken ")
//        val token = AccessToken.getCurrentAccessToken()
//        logD("access token2: $token ")
        userName = profile?.name
    }

    private fun setupLoginManagerCallback(){
        faceLoginManager.registerCallback(faceCallbackManager, object : FacebookCallback<LoginResult> {
                override fun onSuccess(result: LoginResult?) {
                    isLoggedIn = true
                    showToast(getString(R.string.login_success))
                    getFacebookProfile()
                }

                override fun onCancel() {
                    isLoggedIn = false
                    showToast(getString(R.string.log_in_to_proceed))
                }

                override fun onError(error: FacebookException?) {
                    //todo tu sie pokazuje server error na telefonie taty - chyba naprawione,
                    //todo w loginWithFacebookIfConnected() bylo drugie instance LoginManagera, a nie ten z daggera
                    isLoggedIn = false
                    error?.let { logD(it) }
                    showToast(getString(R.string.login_problem_facebook))
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
        setupSpeedDialClick()
    }

    private fun setupSpeedDialClick(){
        speed_dial_login.setOnActionSelectedListener { item ->
            when(item.id){
                R.id.get_recipe_menuItem -> {
                    goToCookingFragmentIfConnected()
                    true
                }
                R.id.facebook_menuItem -> {
                    loginWithFacebookIfConnected()
                    true
                }
                else -> false
            }
        }
    }

    private fun goToCookingFragmentIfConnected(){
        if (network.isConnected.not()) {
            showToast(getString(R.string.connect_to_proceed))
        } else {
            goToCookingFragment()
        }
    }

    private fun goToCookingFragment(){
        val name = userName ?: getString(R.string.not_logged_in)
        val directions = LoginFragmentDirections.actionToCooking(name)
        findNavController().navigate(directions)
    }

    private fun loginWithFacebookIfConnected(){
        if (isLoggedIn){
            showToast(getString(R.string.logged_in_already))
        } else {
            if (network.isConnected.not()) {
                showToast(getString(R.string.connection_problem))
            } else {
                loginWithFacebook()
            }
        }
    }

    private fun loginWithFacebook(){
        faceLoginManager.logInWithReadPermissions(this, mutableListOf("email", "public_profile"))
    }

}