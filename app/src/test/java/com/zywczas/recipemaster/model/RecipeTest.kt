package com.zywczas.recipemaster.model

import com.zywczas.recipemaster.util.MOCKED_API_RESPONSE_BODY
import com.zywczas.recipemaster.util.TestUtil
import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.assertEquals
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.HttpURLConnection
import org.junit.jupiter.api.Assertions.*

internal class RecipeTest {

    @Test
    fun convertIngredientsListToDescription(){
        val expected = "- 3 szklanki mąki pszennej\n" +
                    "- 1 łyżeczka soli\n" +
                "- przyprawy do smaku (oregano, bazylia i słodka papryka)"
        val recipe = TestUtil.recipe1

        recipe.convertIngredientsListToDescription()
        val actual = recipe.ingredientsDescription

        assertEquals(expected, actual)
    }

}