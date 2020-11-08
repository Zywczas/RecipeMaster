package com.zywczas.recipemaster.views

import android.content.Context
import org.junit.Assert.*
import android.os.Looper.getMainLooper
import androidx.core.content.ContextCompat
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
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.login.Login
import com.facebook.login.LoginManager
import com.zywczas.recipemaster.BaseApplication
import com.zywczas.recipemaster.R
import com.zywczas.recipemaster.utilities.NetworkCheck
import io.mockk.*
import io.mockk.impl.annotations.RelaxedMockK
import io.reactivex.rxjava3.core.Flowable
import org.hamcrest.core.IsNot.not
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.junit.runners.Parameterized
import org.robolectric.Shadows.shadowOf
import org.robolectric.annotation.Config
import org.robolectric.annotation.LooperMode
import org.robolectric.pluginapi.Sdk
import org.robolectric.shadows.ShadowToast

@RunWith(AndroidJUnit4::class)
@LooperMode(LooperMode.Mode.PAUSED)
internal class LoginFragmentTest {

    private val app = ApplicationProvider.getApplicationContext<BaseApplication>()
    private val network: NetworkCheck = mockk()
    @RelaxedMockK
    private lateinit var faceLoginManager: LoginManager
    @RelaxedMockK
    private lateinit var faceCallbackManager : CallbackManager
    @RelaxedMockK
    private lateinit var faceToken : AccessToken
    private val fragmentFactory : UniversalFragmentFactory = mockk()

    @Before
    fun setup(){
        MockKAnnotations.init(this)
        every { network.isConnected } returns true
        every { faceToken.isExpired } returns true
        every { fragmentFactory.instantiate(any(), any()) } returns
                LoginFragment(network, faceLoginManager, faceCallbackManager, faceToken)
    }

    @After
    fun close(){
        unmockkAll()
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

    @Test
    fun isSpeedDialWorking(){
        val getRecipe = app.getString(R.string.get_recipe)

        @Suppress("UNUSED_VARIABLE")
        val scenario = launchFragmentInContainer<LoginFragment>(factory = fragmentFactory)
        onView(withId(R.id.speed_dial_login)).perform(click())

        onView(withId(R.id.get_recipe_menuItem)).check(matches(isDisplayed()))
        onView(withId(R.id.facebook_menuItem)).check(matches(isDisplayed()))
        onView(withText(getRecipe)).check(matches(isDisplayed()))
        //todo dac test na napis facebooka
    }


}