package com.zywczas.recipemaster.views

import org.junit.Assert.*
import android.os.Looper.getMainLooper
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.Navigation
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.swipeUp
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.contrib.RecyclerViewActions.scrollToPosition
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.facebook.CallbackManager
import com.facebook.login.Login
import com.facebook.login.LoginManager
import com.zywczas.recipemaster.R
import com.zywczas.recipemaster.utilities.NetworkCheck
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.mockk.verifySequence
import io.reactivex.rxjava3.core.Flowable
import org.hamcrest.core.IsNot.not
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Shadows.shadowOf
import org.robolectric.annotation.LooperMode
import org.robolectric.shadows.ShadowToast

@RunWith(AndroidJUnit4::class)
@LooperMode(LooperMode.Mode.PAUSED)
internal class LoginFragmentTest {

    private val network: NetworkCheck = mockk()
    private val faceLoginManager: LoginManager = mockk(relaxed = true)
    private val faceCallbackManager : CallbackManager = mockk(relaxed = true)
    private val fragmentFactory : UniversalFragmentFactory = mockk()

    @Before
    fun setup(){
        every { network.isConnected } returns true
        every { fragmentFactory.instantiate(any(), any()) } returns
                LoginFragment(network, faceLoginManager, faceCallbackManager)
    }

    @Test
    fun isFragmentInView(){
        @Suppress("UNUSED_VARIABLE")
        val scenario = launchFragmentInContainer<LoginFragment>(factory = fragmentFactory)

        onView(withId(R.id.toolbar_login)).check(matches(isDisplayed()))
        onView(withId(R.id.food_imageView_login)).check(matches(isDisplayed()))
        onView(withId(R.id.appName_textView_login)).check(matches(isDisplayed()))
        onView(withId(R.id.speed_dial_login)).check(matches(isDisplayed()))
    }


}