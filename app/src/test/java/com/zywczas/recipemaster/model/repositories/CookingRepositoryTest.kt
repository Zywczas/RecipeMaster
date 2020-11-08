package com.zywczas.recipemaster.model.repositories

import com.zywczas.recipemaster.R
import com.zywczas.recipemaster.model.webservice.RecipeFromApi
import com.zywczas.recipemaster.model.webservice.RecipeRestApiService
import com.zywczas.recipemaster.util.TestUtil
import com.zywczas.recipemaster.utilities.Resource
import io.mockk.every
import io.mockk.mockk
import io.reactivex.rxjava3.core.Single
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class CookingRepositoryTest {

    private val apiService = mockk<RecipeRestApiService>()
    private val repo = CookingRepository(apiService)
    private val recipe = TestUtil.recipe1
    private val recipeFromApi = TestUtil.recipeFromApi1

    @Nested
    inner class GetRecipe {

        @Test
        fun returnRecipe() {
            val expected = recipe
            val returnedApiResponse = Single.just(recipeFromApi)
            every { apiService.getRecipe() } returns returnedApiResponse

            val actual = repo.getRecipeFromApi().blockingFirst()

            assertEquals(Resource.success(expected), actual)
        }

        @Test
        fun getException_returnError() {
            val expectedMessage = R.string.general_restapi_error
            val apiResponse: Single<RecipeFromApi> = Single.error(Exception())
            every { apiService.getRecipe() } returns apiResponse

            val repoResponse = repo.getRecipeFromApi().blockingFirst()
            val actualMessage = repoResponse.message?.getContentIfNotHandled()
            val actualData = repoResponse.data

            assertEquals(expectedMessage, actualMessage)
            assertEquals(null, actualData)
        }


    }


}