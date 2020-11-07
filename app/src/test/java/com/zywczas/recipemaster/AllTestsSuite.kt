package com.zywczas.recipemaster

import org.junit.platform.runner.JUnitPlatform
import org.junit.platform.suite.api.SelectPackages
import org.junit.runner.RunWith

@RunWith(JUnitPlatform::class)
@SelectPackages(
    "com.zywczas.recipemaster.model",
    "com.zywczas.recipemaster.utilities"
)
class AllTestsSuite

//todo usunac to noname z junit vintage - pewnie bierze klase ktora usunalem z androidTest