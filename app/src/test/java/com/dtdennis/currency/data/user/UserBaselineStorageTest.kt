package com.dtdennis.currency.data.user

import com.dtdennis.currency.core.user.UserBaseline
import com.dtdennis.currency.util.TestSharedPrefs
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

class UserBaselineStorageTest {
    private val testBaseline = UserBaseline("EUR", "Euro", 1.0, emptyMap())
    private val testDefaultBaseline = UserBaseline("DEF", "Default", 0.0, emptyMap())
    private val storage = UserBaselineStorage(TestSharedPrefs())

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