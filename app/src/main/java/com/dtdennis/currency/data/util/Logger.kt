package com.dtdennis.currency.data.util

/**
 * Simple logger interface
 */
interface Logger {
    fun d(tag: String, message: String)
    fun e(tag: String, error: Throwable)
}