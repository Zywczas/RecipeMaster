package com.zywczas.recipemaster.model.webservice

import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.zywczas.recipemaster.util.MOCKED_API_RESPONSE_BODY
import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.AfterClass
import org.junit.Assert
import org.junit.Assert.*
import org.junit.BeforeClass
import org.junit.Test
import org.junit.runner.RunWith
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.HttpURLConnection

@RunWith(AndroidJUnit4ClassRunner::class)
internal class RecipeRestApiServiceTest {

    private val mockWebServer = MockWebServer()
    private lateinit var apiService : RecipeRestApiService

    @BeforeClass
    private fun setup(){
        mockWebServer.start()
        apiService = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(RecipeRestApiService::class.java)
    }

    @AfterClass
    private fun close(){
        mockWebServer.close()
    }

    @Test
    fun getRecipe_success(){
        val response = MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_OK)
            .setBody(MOCKED_API_RESPONSE_BODY)
        mockWebServer.enqueue(response)

        val recipe = apiService.getRecipe().blockingGet()
        val actualFood = recipe.title
        assertEquals("Pizza", actualFood)
    }




}