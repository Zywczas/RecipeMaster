package com.zywczas.recipemaster.views

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.Navigation
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.scrollTo
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.bumptech.glide.RequestManager
import com.zywczas.recipemaster.BaseApplication
import com.zywczas.recipemaster.R
import com.zywczas.recipemaster.model.Recipe
import com.zywczas.recipemaster.model.repositories.CookingRepository
import com.zywczas.recipemaster.util.TestUtil
import com.zywczas.recipemaster.utilities.Resource
import com.zywczas.recipemaster.viewmodels.CookingViewModel
import com.zywczas.recipemaster.viewmodels.UniversalViewModelFactory
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.unmockkAll
import io.reactivex.rxjava3.core.Flowable
import org.hamcrest.CoreMatchers.containsString
import org.hamcrest.CoreMatchers.not
import org.junit.*
import org.junit.Assert.*
import org.junit.runner.RunWith
import org.robolectric.annotation.LooperMode
import org.robolectric.shadows.ShadowAlertDialog

@RunWith(AndroidJUnit4::class)
@LooperMode(LooperMode.Mode.PAUSED)
class CookingFragmentTest {

    private val glide = mockk<RequestManager>(relaxed = true)
    private val repo = mockk<CookingRepository>()
    @MockK
    private lateinit var viewModelFactory: UniversalViewModelFactory
    private val fragmentFactory = mockk<UniversalFragmentFactory>()
    private val bundle = LoginFragmentDirections.actionToCooking("James Blad").arguments
    private val userName = "James Blad"
    private val returnedRecipe = Flowable.just(Resource.success(TestUtil.recipe1))
    private val app = ApplicationProvider.getApplicationContext<BaseApplication>()
    private val navController = TestNavHostController(app)

    //main layout
    private val toolbar = onView(withId(R.id.toolbar_cooking))
    private val foodName = onView(withId(R.id.foodName_textView_cooking))
    private val foodDescription = onView(withId(R.id.foodDescription_textView_cooking))
    private val ingredientsTitle = onView(withId(R.id.ingredientsTitle_textView_cooking))
    private val ingredientsList = onView(withId(R.id.ingredientsList_textView_cooking))
    private val preparingTitle = onView(withId(R.id.preparingTitle_textView_cooking))
    private val preparingSteps = onView(withId(R.id.preparingSteps_textView_cooking))
    private val progressBar = onView(withId(R.id.progressBar_cooking))
    private val imagesTitle = onView(withId(R.id.imagesTitle_textView_cooking))
    private val food0 = onView(withId(R.id.food0_imageView_cooking))
    private val food1 = onView(withId(R.id.food1_imageView_cooking))
    private val food2 = onView(withId(R.id.food2_imageView_cooking))

    //save image dialog
    private val saveImageDialog = onView(withId(R.id.saveImage_dialog))
    private val question = onView(withId(R.id.question_textView_saveDialog))
    private val yes = onView(withId(R.id.yes_textView_dialog))
    private val no = onView(withId(R.id.no_textView_dialog))

    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        every { repo.getRecipeFromApi() } returns returnedRecipe
        every { viewModelFactory.create(CookingViewModel::class.java) } returns CookingViewModel(
            repo
        )
        every {fragmentFactory.instantiate(any(),any())} returns CookingFragment(viewModelFactory, glide)
        navController.setGraph(R.navigation.main_nav_graph)
        navController.setCurrentDestination(R.id.destination_cooking)
    }

    @After
    fun close() {
        unmockkAll()
    }

    @Test
    fun isFragmentInView() {
        @Suppress("UNUSED_VARIABLE")
        val scenario = launchFragmentInContainer(
            fragmentArgs = bundle,
            themeResId = R.style.AppTheme
        ) {
            CookingFragment(viewModelFactory, glide).also { fragment ->
                fragment.viewLifecycleOwnerLiveData.observeForever { viewLifeCycleOwner ->
                    if (viewLifeCycleOwner != null) {
                        Navigation.setViewNavController(fragment.requireView(), navController)
                    }
                }
            }
        }

        toolbar.check(matches(isDisplayed()))
        foodName.check(matches(isDisplayed()))
        foodDescription.check(matches(isDisplayed()))
        ingredientsTitle.check(matches(isDisplayed()))
        ingredientsList.perform(scrollTo()).check(matches(isDisplayed()))
        preparingTitle.perform(scrollTo()).check(matches(isDisplayed()))
        preparingSteps.perform(scrollTo()).check(matches(isDisplayed()))
        progressBar.check(matches(not(isDisplayed())))
        imagesTitle.perform(scrollTo()).check(matches(isDisplayed()))
        food0.perform(scrollTo()).check(matches(isDisplayed()))
        food1.check(matches(isDisplayed()))
        food2.perform(scrollTo()).check(matches(isDisplayed()))
    }

    @Test
    fun isContentDisplayed(){
        val recipe = TestUtil.recipe1
        val toolbarTitle = "${recipe.title} ${app.getString(R.string.recipe)}"

        @Suppress("UNUSED_VARIABLE")
        val scenario = launchFragmentInContainer(
            fragmentArgs = bundle,
            themeResId = R.style.AppTheme
        ) {
            CookingFragment(viewModelFactory, glide).also { fragment ->
                fragment.viewLifecycleOwnerLiveData.observeForever { viewLifeCycleOwner ->
                    if (viewLifeCycleOwner != null) {
                        Navigation.setViewNavController(fragment.requireView(), navController)
                    }
                }
            }
        }

        toolbar.check(matches(hasDescendant(withText(toolbarTitle))))
        foodName.check(matches(withText("${recipe.title}:")))
        foodDescription.check(matches(withText(recipe.foodDescription)))
        ingredientsTitle.check(matches(withText(app.getString(R.string.ingredients))))
        ingredientsList.perform(scrollTo()).check(matches(withText(recipe.ingredientsDescription)))
        preparingTitle.perform(scrollTo()).check(matches(withText(app.getString(R.string.preparing))))
        preparingSteps.perform(scrollTo()).check(matches(withText(recipe.preparingDescription)))
        imagesTitle.perform(scrollTo()).check(matches(withText(app.getString(R.string.images))))
    }

    @Test
    fun isDataFromDirectionsDisplayed(){
        val snackbarText = "${app.getString(R.string.logged_as)} $userName"

        @Suppress("UNUSED_VARIABLE")
        val scenario = launchFragmentInContainer(
            fragmentArgs = bundle,
            themeResId = R.style.AppTheme
        ) {
            CookingFragment(viewModelFactory, glide).also { fragment ->
                fragment.viewLifecycleOwnerLiveData.observeForever { viewLifeCycleOwner ->
                    if (viewLifeCycleOwner != null) {
                        Navigation.setViewNavController(fragment.requireView(), navController)
                    }
                }
            }
        }

        onView(withText(snackbarText)).check(matches(isDisplayed()))
    }

    @Test
    fun fragmentDestroyed_isInstanceStateSavedAndRestored(){
        val snackbarText = "${app.getString(R.string.logged_as)} $userName"
        val recipe = TestUtil.recipe1
        val toolbarTitle = "${recipe.title} ${app.getString(R.string.recipe)}"

        val scenario = launchFragmentInContainer(
            fragmentArgs = bundle,
            themeResId = R.style.AppTheme
        ) {
            CookingFragment(viewModelFactory, glide).also { fragment ->
                fragment.viewLifecycleOwnerLiveData.observeForever { viewLifeCycleOwner ->
                    if (viewLifeCycleOwner != null) {
                        Navigation.setViewNavController(fragment.requireView(), navController)
                    }
                }
            }
        }
        scenario.recreate()

        toolbar.check(matches(hasDescendant(withText(toolbarTitle))))
        foodName.check(matches(withText("${recipe.title}:")))
        foodDescription.check(matches(withText(recipe.foodDescription)))
        ingredientsList.perform(scrollTo()).check(matches(withText(recipe.ingredientsDescription)))
        preparingSteps.perform(scrollTo()).check(matches(withText(recipe.preparingDescription)))
        onView(withText(snackbarText)).check(matches(isDisplayed()))
    }

    @Test
    fun getError_observeMessage(){
        val message = R.string.general_restapi_error
        val expectedMessage = app.getString(R.string.general_restapi_error)
        val returnedError : Flowable<Resource<Recipe>> = Flowable.just(Resource.error(message, null))
        every { repo.getRecipeFromApi() } returns returnedError

        @Suppress("UNUSED_VARIABLE")
        val scenario = launchFragmentInContainer(
            fragmentArgs = bundle,
            themeResId = R.style.AppTheme
        ) {
            CookingFragment(viewModelFactory, glide).also { fragment ->
                fragment.viewLifecycleOwnerLiveData.observeForever { viewLifeCycleOwner ->
                    if (viewLifeCycleOwner != null) {
                        Navigation.setViewNavController(fragment.requireView(), navController)
                    }
                }
            }
        }

        toolbar.check(matches(not(hasDescendant(withText(containsString(""))))))
        foodName.check(matches(withText("")))
        foodDescription.check(matches(withText("")))
        ingredientsList.check(matches(withText("")))
        preparingSteps.check(matches(withText("")))
        onView(withText(expectedMessage)).check(matches(isDisplayed()))
    }

    @Test
    fun clickOnImage_isDialogShowingUp(){
        @Suppress("UNUSED_VARIABLE")
        val scenario = launchFragmentInContainer(
            fragmentArgs = bundle,
            themeResId = R.style.AppTheme
        ) {
            CookingFragment(viewModelFactory, glide).also { fragment ->
                fragment.viewLifecycleOwnerLiveData.observeForever { viewLifeCycleOwner ->
                    if (viewLifeCycleOwner != null) {
                        Navigation.setViewNavController(fragment.requireView(), navController)
                    }
                }
            }
        }
        food0.perform(scrollTo(), click())

        assertEquals(1, ShadowAlertDialog.getShownDialogs().size)
    }

    //todo czy jak sie kliknie dialog to pyta o pozwolenie

    //todo czy jak sie pobierze obrazek to pokazuje dialog

}