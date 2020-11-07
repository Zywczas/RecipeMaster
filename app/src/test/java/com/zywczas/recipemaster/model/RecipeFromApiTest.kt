package com.zywczas.recipemaster.model

import com.zywczas.recipemaster.model.webservice.RecipeFromApi
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*

internal class RecipeFromApiTest {

    private lateinit var recipeFromApi: RecipeFromApi

    @BeforeEach
    fun setup(){
        recipeFromApi = RecipeFromApi(
            "Pizza",
            "Pizza jest potrawą kuchni włoskiej, obecnie szeroko rozpowszechnioną...",
            listOf("3 szklanki mąki pszennej", "1 łyżeczka soli", "przyprawy do smaku (oregano, bazylia i słodka papryka)"),
            listOf("Suche składniki dokładnie mieszamy.", "Drożdże zalawamy ciepłą wodą, olejem i cukrem. Odstawiamy do wyrośnięcia.", "Gotowy płyn wlewamy do mąki i mieszam najpierw łyżką, potem zagniatamy ręką."),
            listOf("http://mooduplabs.com/test/pizza1.jpg", "http://mooduplabs.com/test/pizza2.jpg", "http://mooduplabs.com/test/pizza3.jpg")
        )
    }

    @Test
    fun convertIngredientsListToDescription(){
        val expected = "- 3 szklanki mąki pszennej\n" +
                                "- 1 łyżeczka soli\n" +
                                "- przyprawy do smaku (oregano, bazylia i słodka papryka)"

        recipeFromApi.convertIngredientsListToDescription()
        val actual = recipeFromApi.ingredientsDescription

        assertEquals(expected, actual)
    }

    @Test
    fun convertPreparingStepsToDescription(){
        val expected = "1. Suche składniki dokładnie mieszamy.\n\n" +
                "2. Drożdże zalawamy ciepłą wodą, olejem i cukrem. Odstawiamy do wyrośnięcia.\n\n" +
                "3. Gotowy płyn wlewamy do mąki i mieszam najpierw łyżką, potem zagniatamy ręką."

        recipeFromApi.convertPreparingStepsToDescription()
        val actual = recipeFromApi.preparingDescription

        assertEquals(expected, actual)
    }

}