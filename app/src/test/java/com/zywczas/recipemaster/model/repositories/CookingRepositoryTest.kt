package com.zywczas.recipemaster.model.repositories

import com.zywczas.recipemaster.model.webservice.RecipeFromApi
import com.zywczas.recipemaster.model.webservice.RecipeRestApiService
import com.zywczas.recipemaster.util.TestUtil
import io.mockk.mockk
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Nested

internal class CookingRepositoryTest {

    private val apiService = mockk<RecipeRestApiService>()
    private val repo = CookingRepository(apiService)
    private val recipe = TestUtil.recipeFromApi1

    @Nested
    inner class GetRecipeFromApiFromApi {

        @Test
        fun returnRecipe() {
            val expected = RecipeFromApi(
                "Pizza",
                "Pizza jest potrawą kuchni włoskiej, obecnie szeroko rozpowszechnioną...",
                listOf("3 szklanki mąki pszennej", "1 łyżeczka soli", "przyprawy do smaku (oregano, bazylia i słodka papryka)"),
                listOf("Suche składniki dokładnie mieszamy.", "Drożdże zalawamy ciepłą wodą, olejem i cukrem. Odstawiamy do wyrośnięcia.", "Gotowy płyn wlewamy do mąki i mieszam najpierw łyżką, potem zagniatamy ręką."),
                listOf("http://mooduplabs.com/test/pizza1.jpg", "http://mooduplabs.com/test/pizza2.jpg", "http://mooduplabs.com/test/pizza3.jpg")
            )
            expected.ingredientsDescription

        }
    }





}