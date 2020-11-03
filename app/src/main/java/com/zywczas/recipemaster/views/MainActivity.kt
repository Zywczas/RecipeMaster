package com.zywczas.recipemaster.views

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.zywczas.recipemaster.R
import dagger.android.AndroidInjection
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var fragmentsFactory: UniversalFragmentsFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        supportFragmentManager.fragmentFactory = fragmentsFactory
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

}