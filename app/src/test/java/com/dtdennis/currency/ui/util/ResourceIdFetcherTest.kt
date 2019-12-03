package com.dtdennis.currency.ui.util

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.dtdennis.currency.R
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class ResourceIdFetcherTest {
    val context = ApplicationProvider.getApplicationContext<Context>()

    @Test
    fun Test_GetDrawableId() {
        val launcherName = "ic_launcher_background"

        val resId = ResourceIdFetcher.getDrawableResIdFromResName(context, launcherName, 0)

        assertThat(resId, equalTo(R.drawable.ic_launcher_background))
    }

    @Test
    fun Test_DefaultReturned() {
        val badName = "abc"
        val default = R.drawable.ic_launcher_background

        val resId = ResourceIdFetcher.getDrawableResIdFromResName(context, badName, default)

        assertThat(resId, equalTo(default))
    }
}