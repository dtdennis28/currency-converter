package com.dtdennis.currency.ui.util

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.dtdennis.currency.R
import junit.framework.Assert.fail
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class ResourceIdFetcherTest {
    val context = ApplicationProvider.getApplicationContext<Context>()

    @Test
    fun Should_ReturnCorrectResId_When_ValidResource() {
        val launcherName = "ic_launcher_background"

        val resId = ResourceIdFetcher.getDrawableResIdFromResName(context, launcherName, 0)

        assertThat(resId, equalTo(R.drawable.ic_launcher_background))
    }

    @Test
    fun Should_ReturnDefaultValue_When_ResourceNotFound() {
        val badName = "abc"
        val default = R.drawable.ic_launcher_background

        val resId = ResourceIdFetcher.getDrawableResIdFromResName(context, badName, default)

        assertThat(resId, equalTo(default))
    }

    @Test
    fun ShouldNot_ThrowError_When_NoDefaultGiven() {
        val badName = "abc"

        try {
            ResourceIdFetcher.getDrawableResIdFromResName(context, badName)
        } catch (error: Throwable) {
            fail("Should not throw any errors! Should just return a dumb value like 0")
        }
    }
}