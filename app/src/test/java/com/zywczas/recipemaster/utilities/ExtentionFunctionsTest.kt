package com.zywczas.recipemaster.utilities

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class ExtentionFunctionsTest{

    @Test
    fun lazyAndroid() {
        val expected = "It's working!!"

        val actual by lazyAndroid { "It's working!!" }

        assertEquals(expected, actual)
    }
}