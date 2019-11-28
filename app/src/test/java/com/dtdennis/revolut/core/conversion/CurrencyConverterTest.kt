package com.dtdennis.revolut.core.conversion

import com.dtdennis.revolut.util.mothers.CurrencyRatesManifestMother
import junit.framework.Assert.fail
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

class CurrencyConverterTest {
    private val testRatesMap = CurrencyRatesManifestMother.DEFAULT_CURRENCY_RATES_MAP

    private val badCode = "ABC"
    private val goodCode = "CAD"
    private val goodCodeRate = testRatesMap[goodCode]!!

    private val converter = CurrencyConverter(testRatesMap)

    @Test
    fun TestPreconditions() {
        assertThat(testRatesMap.isNotEmpty(), equalTo(true))
        assertThat(testRatesMap.contains(badCode), equalTo(false))
        assertThat(testRatesMap.contains(goodCode), equalTo(true))
        assertThat(testRatesMap[goodCode], equalTo(goodCodeRate))
    }

    /**
     * Unnecessary with Kotlin's strict nullness enforcements...
     * Nevertheless, why not test
     */
    @Test
    fun Should_ThrowError_When_NullCode() {
        // Given a null code as input
        // When convert
        // Then throw error

        try {
            converter.convert(1.0, null!!)
            fail("CurrencyConverter should throw error if null currency code is given")
        } catch (error: Throwable) {
            // Pass test
        }
    }

    @Test
    fun Should_ThrowError_When_InvalidCode() {
        // Given an invalid code as input
        // When convert
        // Then throw error

        try {
            converter.convert(1.0, badCode)
            fail("CurrencyConverter should throw error if invalid currency code is given")
        } catch (error: Throwable) {
            // Pass test
        }
    }

    @Test
    fun Should_MultiplyValueByRate_When_CodeExists() {
        // Given a valid code as input, and a value to be converted
        val originalValue = 1.0

        // When convert
        val convertedValue = converter.convert(originalValue, goodCode)

        // Then value is as expected
        assertThat(convertedValue, equalTo(goodCodeRate * originalValue))
    }
}