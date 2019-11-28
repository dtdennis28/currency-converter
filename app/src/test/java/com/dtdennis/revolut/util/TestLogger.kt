package com.dtdennis.revolut.util

import com.dtdennis.revolut.data.util.Logger

class TestLogger : Logger {
    override fun d(tag: String, message: String) {
        println("$tag/ $message")
    }

    override fun e(tag: String, error: Throwable) {
        println(tag)
        error.printStackTrace()
    }
}