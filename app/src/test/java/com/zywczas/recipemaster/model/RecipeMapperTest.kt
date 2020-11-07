package com.zywczas.recipemaster.model

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

class RecipeMapperTest {

    @Test
    fun toRecipe_recipeFromApi(){
        val expected = TestUtil.recipe1
        val recipeFromApi = TestUtil.recipeFromApi1

        val actual = toRecipe(recipeFromApi)

        assertEquals(expected, actual)
    }

}