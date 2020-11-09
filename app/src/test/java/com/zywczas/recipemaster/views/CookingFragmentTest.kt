package com.zywczas.recipemaster.views

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.scrollTo
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.bumptech.glide.RequestManager
import com.zywczas.recipemaster.R
import com.zywczas.recipemaster.model.repositories.CookingRepository
import com.zywczas.recipemaster.util.TestUtil
import com.zywczas.recipemaster.utilities.Resource
import com.zywczas.recipemaster.viewmodels.CookingViewModel
import com.zywczas.recipemaster.viewmodels.UniversalViewModelFactory
import io.mockk.*
import io.mockk.impl.annotations.MockK
import io.reactivex.rxjava3.core.Flowable
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.LooperMode

@RunWith(AndroidJUnit4::class)
@LooperMode(LooperMode.Mode.PAUSED)
class CookingFragmentTest {

    private val glide = mockk<RequestManager>(relaxed = true)
    private val repo = mockk<CookingRepository>()
    @MockK
    private lateinit var viewModelFactory : UniversalViewModelFactory
    private val fragmentFactory = mockk<UniversalFragmentFactory>()
    private val bundle = LoginFragmentDirections.actionToCooking("James Blad").arguments
    private val returnedRecipe = Flowable.just(Resource.success(TestUtil.recipe1))

    @Before
    fun setup(){
        MockKAnnotations.init(this)
        every { repo.getRecipeFromApi() } returns returnedRecipe
        every { viewModelFactory.create(CookingViewModel::class.java) } returns CookingViewModel(repo)
        every { fragmentFactory.instantiate(any(), any()) } returns CookingFragment(viewModelFactory, glide)
    }

    @After
    fun close(){
        unmockkAll()
    }

    @Test
    fun isFragmentInView(){
        @Suppress("UNUSED_VARIABLE")
        val scenario = launchFragmentInContainer<CookingFragment>(
            factory = fragmentFactory,
            fragmentArgs = bundle,
            themeResId = R.style.AppTheme
        )

        onView(withId(R.id.toolbar_cooking)).check(matches(isDisplayed()))
        onView(withId(R.id.foodName_textView_cooking)).check(matches(isDisplayed()))
        onView(withId(R.id.foodDescription_textView_cooking)).check(matches(isDisplayed()))
        onView(withId(R.id.ingredientsTitle_textView_cooking)).check(matches(isDisplayed()))
        onView(withId(R.id.ingredientsList_textView_cooking)).perform(scrollTo()).check(matches(isDisplayed()))
        onView(withId(R.id.preparingTitle_textView_cooking)).perform(scrollTo()).check(matches(isDisplayed()))
        onView(withId(R.id.preparingSteps_textView_cooking)).perform(scrollTo()).check(matches(isDisplayed()))
        onView(withId(R.id.imagesTitle_textView_cooking)).perform(scrollTo()).check(matches(isDisplayed()))
        onView(withId(R.id.food0_imageView_cooking)).perform(scrollTo()).check(matches(isDisplayed()))
        onView(withId(R.id.food1_imageView_cooking)).check(matches(isDisplayed()))
        onView(withId(R.id.food2_imageView_cooking)).perform(scrollTo()).check(matches(isDisplayed()))
    }


}