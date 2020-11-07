package com.zywczas.recipemaster.util

import com.zywczas.recipemaster.model.Recipe

object TestUtil {

    val recipe1 = Recipe(
        "Pizza",
        "Pizza jest potrawą kuchni włoskiej, obecnie szeroko rozpowszechnioną...",
        listOf("3 szklanki mąki pszennej", "1 łyżeczka soli", "przyprawy do smaku (oregano, bazylia i słodka papryka)"),
        listOf("Suche składniki dokładnie mieszamy.", "Drożdże zalawamy ciepłą wodą, olejem i cukrem. Odstawiamy do wyrośnięcia.", "Gotowy płyn wlewamy do mąki i mieszam najpierw łyżką, potem zagniatamy ręką."),
        listOf("http://mooduplabs.com/test/pizza1.jpg", "http://mooduplabs.com/test/pizza2.jpg", "http://mooduplabs.com/test/pizza3.jpg")
    )
}