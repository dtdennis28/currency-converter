package com.dtdennis.currency.data.user

import com.dtdennis.currency.core.user.UserBaseline
import com.dtdennis.currency.util.TestSharedPrefs
import org.junit.Test

class UserBaselineRepositoryImplTest {
    private val EXPECTED_DEFAULT_BASELINE = UserBaseline(
        "EUR",
        "Euro",
        1.0,
        mapOf(
            "EUR" to 0
        )
    )

    private val storage = UserBaselineStorage(TestSharedPrefs())
    private val repo = UserBaselineRepositoryImpl(storage)

    @Test
    fun Should_ReturnExpectedDefaultBaseline_When_NotSet() {
        // Given no baseline set
        // When get
        // Then is as expected
        repo
            .get()
            .test()
            .assertValue(EXPECTED_DEFAULT_BASELINE)
    }

    @Test
    fun Should_ReturnStoredBaseline() {
        // Given baseline was set
        val baseline = UserBaseline("USD", "US Dollar", 10.0, mapOf())
        repo
            .set(baseline)
            .test()
            .assertComplete()

        // When get
        // Then is as expected
        repo
            .get()
            .test()
            .assertValue(baseline)
    }
}