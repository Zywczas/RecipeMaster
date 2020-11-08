package com.zywczas.recipemaster.model.webservice

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

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class RecipeRestApiServiceTest {

    private val mockWebServer = MockWebServer()
    private lateinit var apiService : RecipeRestApiService

    @BeforeAll
    private fun setup(){
        mockWebServer.start()
        apiService = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(RecipeRestApiService::class.java)
    }

    @AfterAll
    private fun close(){
        mockWebServer.close()
    }

    @Nested
    inner class GetRecipeFromApiFromApi {

        @Test
        fun success(){
            val response = MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_OK)
                .setBody(MOCKED_API_RESPONSE_BODY)
            mockWebServer.enqueue(response)

            val recipe = apiService.getRecipe().blockingGet()
            val actualFood = recipe.title

            assertEquals("Pizza", actualFood)
        }

        @Test
        fun testGsonConverter(){
            val expectedRecipe = TestUtil.recipeFromApi1
            val response = MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_OK)
                .setBody(MOCKED_API_RESPONSE_BODY)
            mockWebServer.enqueue(response)

            val actualRecipe = apiService.getRecipe().blockingGet()

            assertEquals(expectedRecipe, actualRecipe)
        }

        @Test
        fun getHttpError_throwException(){
            val response = MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_UNAUTHORIZED)
                .setBody(MOCKED_API_RESPONSE_BODY)
            mockWebServer.enqueue(response)

            assertThrows<HttpException> { apiService.getRecipe().blockingGet() }
        }


    }



}