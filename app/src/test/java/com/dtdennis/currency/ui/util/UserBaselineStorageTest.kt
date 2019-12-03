package com.dtdennis.currency.ui.util

import androidx.test.core.app.ApplicationProvider
import com.dtdennis.currency.ui.entities.UserBaseline
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class UserBaselineStorageTest {
    private val testBaseline = UserBaseline("EUR", "Euro", 1.0, emptyMap())
    private val testDefaultBaseline = UserBaseline("DEF", "Default", 0.0, emptyMap())
    private val storage = UserBaselineStorage(ApplicationProvider.getApplicationContext())

    @Test
    fun Should_ReturnBaseline_When_Stored() {
        // Given the test baseline has been stored
        storage.setBaseline(testBaseline)

        // When it is retrieved
        val retrievedBaseline = storage.getBaseline(testDefaultBaseline)

        // Then it matches
        assertThat(retrievedBaseline, equalTo(testBaseline))
    }

    @Test
    fun Should_ReturnDefaultBaseline_When_NotStored() {
        // Given test baseline has not been stored
        // When fetch
        val retrievedBaseline = storage.getBaseline(testDefaultBaseline)

        // Then the retrieved matches the default
        assertThat(retrievedBaseline, equalTo(testDefaultBaseline))
    }
}