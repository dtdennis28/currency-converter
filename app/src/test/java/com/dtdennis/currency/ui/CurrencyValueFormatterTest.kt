package com.dtdennis.currency.ui

import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

class CurrencyValueFormatterTest {
    @Test
    fun Should_FormatValue_Correctly() {
        assertThat(CurrencyValueFormatter.format(1.0), equalTo("1.000"))
    }
}