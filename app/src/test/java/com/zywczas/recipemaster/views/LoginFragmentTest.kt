package com.zywczas.recipemaster.views

import androidx.fragment.app.Fragment
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.Navigation
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.facebook.CallbackManager
import com.facebook.login.LoginManager
import com.zywczas.recipemaster.BaseApplication
import com.zywczas.recipemaster.R
import com.zywczas.recipemaster.SessionManager
import io.mockk.*
import io.mockk.impl.annotations.RelaxedMockK
import org.hamcrest.Matchers.not
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.LooperMode

@RunWith(AndroidJUnit4::class)
@LooperMode(LooperMode.Mode.PAUSED)
class LoginFragmentTest {

    private val session: SessionManager = mockk()
    @RelaxedMockK
    private lateinit var faceLoginManager: LoginManager
    @RelaxedMockK
    private lateinit var faceCallbackManager : CallbackManager
    private val fragmentFactory : UniversalFragmentFactory = mockk()
    private val app = ApplicationProvider.getApplicationContext<BaseApplication>()
    private val getRecipe = app.getString(R.string.get_recipe)
    private val faceLogin = app.getString(R.string.login_with_facebook)

    @Before
    fun setup(){
        MockKAnnotations.init(this)
        every { session.isConnected } returns true
        every { session.isLoggedIn } returns true
        every { fragmentFactory.instantiate(any(), any()) } returns
                LoginFragment(session, faceLoginManager, faceCallbackManager)
    }

    @After
    fun close(){
        unmockkAll()
    }

    @Test
    fun isFragmentInView(){
        @Suppress("UNUSED_VARIABLE")
        val scenario = launchFragmentInContainer<LoginFragment>(
            factory = fragmentFactory,
            themeResId = R.style.AppTheme
        )

        onView(withId(R.id.toolbar_login)).check(matches(isDisplayed()))
        onView(withId(R.id.food_imageView_login)).check(matches(isDisplayed()))
        onView(withId(R.id.appName_textView_login)).check(matches(isDisplayed()))
        onView(withId(R.id.speedDial_login)).check(matches(isDisplayed()))
        onView(withText(getRecipe)).check(matches(not(isDisplayed())))
        onView(withText(faceLogin)).check(matches(not(isDisplayed())))
    }

    @Test
    fun isContentDisplayed(){
        val appName = app.getString(R.string.app_name)

        @Suppress("UNUSED_VARIABLE")
        val scenario = launchFragmentInContainer<LoginFragment>(
            factory = fragmentFactory,
            themeResId = R.style.AppTheme
        )

        onView(withId(R.id.appName_textView_login)).check(matches(withText(appName)))
        onView(withId(R.id.toolbar_login)).check(matches(hasDescendant(withText(appName))))
    }

    @Test
    fun isSpeedDialWorking(){
        @Suppress("UNUSED_VARIABLE")
        val scenario = launchFragmentInContainer<LoginFragment>(
            factory = fragmentFactory,
            themeResId = R.style.AppTheme
        )
        onView(withId(R.id.speedDial_login)).perform(click())

        onView(withId(R.id.get_recipe_menuItem)).check(matches(isDisplayed()))
        onView(withId(R.id.facebook_menuItem)).check(matches(isDisplayed()))
        onView(withText(getRecipe)).check(matches(isDisplayed()))
        onView(withText(faceLogin)).check(matches(isDisplayed()))
    }

    @Test
    fun fragmentDestroyed_isInstanceStateSavedAndRestored(){
        val scenario = launchFragmentInContainer<LoginFragment>(
            factory = fragmentFactory,
            themeResId = R.style.AppTheme
        )
        onView(withId(R.id.speedDial_login)).perform(click())
        scenario.recreate()

        onView(withId(R.id.toolbar_login)).check(matches(isDisplayed()))
        onView(withId(R.id.food_imageView_login)).check(matches(isDisplayed()))
        onView(withId(R.id.appName_textView_login)).check(matches(isDisplayed()))
        onView(withId(R.id.speedDial_login)).check(matches(isDisplayed()))
        onView(withText(getRecipe)).check(matches(isDisplayed()))
        onView(withText(faceLogin)).check(matches(isDisplayed()))
    }

    @Test
    fun isLoginClickTriggered(){
        every { session.isLoggedIn } returns false

        @Suppress("UNUSED_VARIABLE")
        val scenario = launchFragmentInContainer<LoginFragment>(
            factory = fragmentFactory,
            themeResId = R.style.AppTheme
        )
        onView(withId(R.id.speedDial_login)).perform(click())
        onView(withId(R.id.facebook_menuItem)).perform(click())

        verify(exactly = 1) { faceLoginManager.logInWithReadPermissions(any<Fragment>(), any()) }
    }

    @Test
    fun navigationToCookingFragment(){
        val expectedArgument = LoginFragmentDirections.actionToCooking("James Blad").arguments["userName"] as String
        val navController = TestNavHostController(app)
        navController.setGraph(R.navigation.main_nav_graph)
        navController.setCurrentDestination(R.id.destination_login)

        val scenario = launchFragmentInContainer<LoginFragment>(
            factory = fragmentFactory,
            themeResId = R.style.AppTheme
        )
        scenario.onFragment { Navigation.setViewNavController(it.requireView(), navController) }
        onView(withId(R.id.speedDial_login)).perform(click())
        onView(withId(R.id.get_recipe_menuItem)).perform(click())
        val actualArgument = navController.backStack.last().arguments?.get("userName")

        assertEquals(R.id.destination_cooking, navController.currentDestination?.id)
        assertEquals(expectedArgument, actualArgument)
    }

    //todo czy jak sie kliknie speed dial to wygasza tlo


}