package com.zywczas.recipemaster.util

import com.zywczas.recipemaster.model.Recipe
import com.zywczas.recipemaster.model.webservice.RecipeFromApi

object TestUtil {

    val recipeFromApi1 = RecipeFromApi(
        "Pizza",
        "Pizza jest potrawą kuchni włoskiej, obecnie szeroko rozpowszechnioną...",
        listOf("3 szklanki mąki pszennej", "1 łyżeczka soli", "przyprawy do smaku (oregano, bazylia i słodka papryka)"),
        listOf("Suche składniki dokładnie mieszamy.", "Drożdże zalawamy ciepłą wodą, olejem i cukrem. Odstawiamy do wyrośnięcia.", "Gotowy płyn wlewamy do mąki i mieszam najpierw łyżką, potem zagniatamy ręką."),
        listOf("http://mooduplabs.com/test/pizza1.jpg", "http://mooduplabs.com/test/pizza2.jpg", "http://mooduplabs.com/test/pizza3.jpg")
    )

    val recipe1 = Recipe(
        "Pizza",
        "Pizza jest potrawą kuchni włoskiej, obecnie szeroko rozpowszechnioną...",
        "- 3 szklanki mąki pszennej\n" +
                "- 1 łyżeczka soli\n" +
                "- przyprawy do smaku (oregano, bazylia i słodka papryka)",
        "1. Suche składniki dokładnie mieszamy.\n\n" +
                "2. Drożdże zalawamy ciepłą wodą, olejem i cukrem. Odstawiamy do wyrośnięcia.\n\n" +
                "3. Gotowy płyn wlewamy do mąki i mieszam najpierw łyżką, potem zagniatamy ręką.",
        listOf("http://mooduplabs.com/test/pizza1.jpg", "http://mooduplabs.com/test/pizza2.jpg", "http://mooduplabs.com/test/pizza3.jpg")
    )


    val recipe2 = Recipe(
        "Pomidorowa",
        "Pomidorowa też jest dobra.",
        "- wlej wodę do garnka i gotuj",
        "1. Trochę posól.",
        listOf("http://mooduplabs.com/test/pizza1.jpg")
    )

}