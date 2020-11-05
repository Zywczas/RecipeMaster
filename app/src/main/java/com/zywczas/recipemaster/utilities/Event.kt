package com.zywczas.recipemaster.utilities

/**
 * Used as a wrapper for data that is exposed via a LiveData that represents an event.
 */
class Event<out T> constructor(private val content: T) {

    private var hasBeenHandled = false

    fun getContentIfNotHandled(): T? {
        return if (hasBeenHandled) {
            null
        } else {
            hasBeenHandled = true
            content
        }
    }

}