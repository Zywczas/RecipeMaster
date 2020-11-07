package com.zywczas.recipemaster.model.webservice

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.zywczas.recipemaster.util.MOCKED_API_RESPONSE_BODY
import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.assertEquals
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.HttpURLConnection

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class ApiServiceTest {

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
    inner class GetRecipe {
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
    }

}