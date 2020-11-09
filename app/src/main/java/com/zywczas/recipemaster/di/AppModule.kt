package com.zywczas.recipemaster.di

import android.app.Application
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import com.facebook.CallbackManager
import com.facebook.login.LoginManager
import com.zywczas.recipemaster.R
import com.zywczas.recipemaster.model.webservice.RecipeRestApiService
import com.zywczas.recipemaster.utilities.BASE_URL
import dagger.Module
import dagger.Provides
import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
object AppModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Provides
    @Singleton
    fun provideRecipeRestApiService(retrofit: Retrofit) : RecipeRestApiService =
        retrofit.create(RecipeRestApiService::class.java)

    @Provides
    @Singleton
    fun provideRequestOptions() : RequestOptions = RequestOptions()
        .placeholder(R.drawable.white_background)
        .error(R.drawable.error_image)

    @Provides
    @Singleton
    fun provideGlide(app: Application, options: RequestOptions) : RequestManager =
        Glide.with(app)
            .setDefaultRequestOptions(options)

    @Provides
    @Singleton
    fun provideFacebookLoginManager() : LoginManager = LoginManager.getInstance()

    @Provides
    @Singleton
    fun provideFacebookCallbackManager() : CallbackManager = CallbackManager.Factory.create()

}