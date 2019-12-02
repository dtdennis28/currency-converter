package com.dtdennis.currency.ui.util

/**
 * NOTE: This is purely copy/pasted from here:
 * https://gist.github.com/JoseAlcerreca/5b661f1800e1e654f07cc54fe87441af#file-event-kt
 */
open class LiveDataEvent<out T>(private val content: T) {
    var hasBeenHandled = false
        private set // Allow external read but not write

    /**
     * Returns the content and prevents its use again.
     */
    fun getContentIfNotHandled(): T? {
        return if (hasBeenHandled) {
            null
        } else {
            hasBeenHandled = true
            content
        }
    }

    /**
     * Returns the content, even if it's already been handled.
     */
    fun peekContent(): T = content
}