package com.dtdennis.currency.util

import android.util.Log
import com.dtdennis.currency.data.util.Logger

/**
 * Android-based logger for runtime
 */
class AppLogger : Logger {
    override fun d(tag: String, message: String) {
        Log.d(tag, message)
    }

    override fun e(tag: String, error: Throwable) {
        Log.e(tag, error.message, error)
    }
}