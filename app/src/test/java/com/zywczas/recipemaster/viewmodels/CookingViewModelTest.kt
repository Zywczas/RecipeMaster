package com.zywczas.recipemaster.viewmodels

import com.zywczas.recipemaster.R
import com.zywczas.recipemaster.model.Recipe
import com.zywczas.recipemaster.model.repositories.CookingRepository
import com.zywczas.recipemaster.util.LiveDataTestUtil
import com.zywczas.recipemaster.util.TestUtil
import com.zywczas.recipemaster.utilities.InstantExecutorExtension
import com.zywczas.recipemaster.utilities.Resource
import io.mockk.every
import io.mockk.mockk
import io.reactivex.rxjava3.core.Flowable
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(InstantExecutorExtension::class)
class CookingViewModelTest {

    private lateinit var viewModel : CookingViewModel
    private val repo = mockk<CookingRepository>()
    private val recipe = TestUtil.recipe1

    @BeforeEach
    private fun setup(){
        viewModel = CookingViewModel(repo)
    }

    @Nested
    inner class GetRecipeOnViewModelInit {

        @Test
        fun observeRecipe() {
            val expected = recipe
            every { repo.getRecipeFromApi() } returns Flowable.just(Resource.success(recipe))

            viewModel.getRecipeOnViewModelInit()
            val actual = LiveDataTestUtil.getValue(viewModel.recipe)

            assertEquals(Resource.success(expected), actual)
        }

        @Test
        fun getRecipeAgain_observeNoChange(){
            val expected = recipe
            every { repo.getRecipeFromApi() } returns
                    Flowable.just(Resource.success(recipe)) andThen
                    Flowable.just(Resource.success(TestUtil.recipe2))

            viewModel.getRecipeOnViewModelInit()
            val firstRecipe = LiveDataTestUtil.getValue(viewModel.recipe)
            viewModel.getRecipeOnViewModelInit()
            val actual = LiveDataTestUtil.getValue(viewModel.recipe)

            assertEquals(Resource.success(expected), actual)
        }

        @Test
        fun getError_observeError(){
            val expectedMessage = R.string.general_restapi_error
            val returnedError :Flowable<Resource<Recipe>> =
                Flowable.just(Resource.error(R.string.general_restapi_error, null))
            every { repo.getRecipeFromApi() } returns returnedError

            viewModel.getRecipeOnViewModelInit()
            val value = LiveDataTestUtil.getValue(viewModel.recipe)
            val actualMessage = value.message?.getContentIfNotHandled()
            val actualData = value.data

            assertEquals(expectedMessage, actualMessage)
            assertEquals(null, actualData)
        }

    }

}