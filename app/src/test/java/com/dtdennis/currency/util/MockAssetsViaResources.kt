package com.dtdennis.currency.util

import android.content.res.AssetManager
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doAnswer
import com.nhaarman.mockitokotlin2.mock
import java.io.File

object MockAssetsViaResources {
    val assetManager = mock<AssetManager> {
        on { open(any()) } doAnswer {
            val file = File("src/test/resources/${it.arguments[0]}")

            file.inputStream()
        }
    }
}