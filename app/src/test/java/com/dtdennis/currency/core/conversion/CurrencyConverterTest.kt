package com.dtdennis.currency.core.conversion

import com.dtdennis.currency.util.mothers.CurrencyRatesManifestMother
import junit.framework.Assert.fail
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

class CurrencyConverterTest {
    private val testRatesMap = CurrencyRatesManifestMother.DEFAULT_CURRENCY_RATES_MAP

    private val badCode1 = "ABC"
    private val badCode2 = "DEF"
    private val goodCode1 = "CAD"
    private val goodCode2 = "GBP"

    private val goodCodeRate1 = testRatesMap[goodCode1]!!
    private val goodCodeRate2 = testRatesMap[goodCode2]!!

    private val converter = CurrencyConverter(testRatesMap)

    @Test
    fun TestPreconditions() {
        assertThat(testRatesMap.isNotEmpty(), equalTo(true))

        assertThat(testRatesMap.contains(badCode1), equalTo(false))
        assertThat(testRatesMap.contains(badCode2), equalTo(false))

        assertThat(testRatesMap.contains(goodCode1), equalTo(true))
        assertThat(testRatesMap.contains(goodCode2), equalTo(true))

        assertThat(testRatesMap[goodCode1], equalTo(goodCodeRate1))
        assertThat(testRatesMap[goodCode2], equalTo(goodCodeRate2))
    }

    @Test
    fun Should_ThrowError_When_InvalidFromCode() {
        // Given an invalid code as input
        // When convert
        // Then throw error

        try {
            converter.convert(1.0, badCode1, goodCode1)
            fail("CurrencyConverter should throw error if invalid from code is given")
        } catch (error: Throwable) {
            // Pass test
        }
    }

    @Test
    fun Should_ThrowError_When_InvalidToCode() {
        // Given an invalid code as input
        // When convert
        // Then throw error

        try {
            converter.convert(1.0, goodCode1, badCode1)
            fail("CurrencyConverter should throw error if invalid from code is given")
        } catch (error: Throwable) {
            // Pass test
        }
    }

    @Test
    fun Should_ThrowError_When_InvalidFromAndToCode() {
        // Given an invalid code as input
        // When convert
        // Then throw error

        try {
            converter.convert(1.0, badCode1, badCode2)
            fail("CurrencyConverter should throw error if invalid from code is given")
        } catch (error: Throwable) {
            // Pass test
        }
    }

    @Test
    fun Should_CorrectlyCalculateConversion_When_GoodCodes() {
        // Given a valid code as input, and a value to be converted
        val originalValue = 2.5

        // When convert
        val convertedValue = converter.convert(originalValue, goodCode1, goodCode2)

        // Then value is as expected
        /*
            (Fv / Fr) = (Tv / Tr)
            (Fv / Fr) * Tr = Tv
         */
        val expectedConvertedValue = (originalValue / goodCodeRate1) * goodCodeRate2

        assertThat(convertedValue, equalTo(expectedConvertedValue))
    }
}