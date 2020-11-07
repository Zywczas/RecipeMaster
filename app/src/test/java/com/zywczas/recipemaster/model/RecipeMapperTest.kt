package com.zywczas.recipemaster.model

import com.zywczas.recipemaster.util.TestUtil
import org.junit.jupiter.api.Assertions.assertEquals
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