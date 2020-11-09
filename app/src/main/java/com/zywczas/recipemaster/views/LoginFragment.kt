package com.zywczas.recipemaster.views

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.Profile
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.leinardi.android.speeddial.SpeedDialView
import com.zywczas.recipemaster.R
import com.zywczas.recipemaster.SessionManager
import com.zywczas.recipemaster.utilities.logD
import com.zywczas.recipemaster.utilities.showToast
import kotlinx.android.synthetic.main.fragment_login.*
import javax.inject.Inject

class LoginFragment @Inject constructor(
    private val session: SessionManager,
    private val faceLoginManager: LoginManager,
    private val faceCallbackManager : CallbackManager,
) : Fragment(R.layout.fragment_login) {

    private var userName: String? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkIfLoggedInAndGetProfile()
        setupLoginManagerCallback()
        setupUi()
        setupOnClickListeners()
    }

    private fun checkIfLoggedInAndGetProfile(){
        if (session.isLoggedIn) {
            getFacebookProfile()
        }
    }

    private fun getFacebookProfile(){
        val profile = Profile.getCurrentProfile()
        userName = profile?.name
    }

    private fun setupLoginManagerCallback(){
        faceLoginManager.registerCallback(faceCallbackManager, object : FacebookCallback<LoginResult> {
                override fun onSuccess(result: LoginResult?) {
                    session.isLoggedIn = true
                    showToast(getString(R.string.login_success))
                    getFacebookProfile()
                }

                override fun onCancel() {
                    session.isLoggedIn = false
                    showToast(getString(R.string.log_in_to_proceed))
                }

                override fun onError(error: FacebookException?) {
                    session.isLoggedIn = false
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
        speedDial_login.inflate(R.menu.menu_speed_dial)
    }

    private fun setupOnClickListeners(){
        setupSpeedDialMainBtnClick()
        setupSpeedDialMenuClick()
    }

    private fun setupSpeedDialMainBtnClick(){
        speedDial_login.setOnChangeListener(object : SpeedDialView.OnChangeListener{
            override fun onMainActionSelected(): Boolean {
                return false
            }
            override fun onToggleChanged(isOpen: Boolean) {
                dimOrRestoreBackground(isOpen)
            }
        })
    }

    private fun dimOrRestoreBackground(isDialOpen : Boolean){
        val window = requireActivity().window
        if (isDialOpen){
            window.statusBarColor = ContextCompat.getColor(requireContext(), R.color.colorPrimaryDark_03)
            constraintLayout_login.alpha = 0.3F
        } else {
            window.statusBarColor = ContextCompat.getColor(requireContext(), R.color.colorPrimaryDark)
            constraintLayout_login.alpha = 1F
        }
    }

    private fun setupSpeedDialMenuClick(){
        speedDial_login.setOnActionSelectedListener { item ->
            when(item.id){
                R.id.get_recipe_menuItem -> {
                    speedDial_login.close()
                    goToCookingFragmentIfConnected()
                    true
                }
                R.id.facebook_menuItem -> {
                    speedDial_login.close()
                    loginWithFacebookIfConnected()
                    true
                }
                else -> false
            }
        }
    }

    private fun goToCookingFragmentIfConnected(){
        if (session.isConnected.not()) {
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
        if (session.isLoggedIn){
            showToast(getString(R.string.logged_in_already))
        } else {
            if (session.isConnected.not()) {
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