package org.mozilla.social.common.utils

import kotlin.test.Test
import kotlin.test.assertEquals

class CountingTest {
    @Test
    fun testCountingValues() {
        assertEquals(20L.toShortenedStringValue(), "20")
        assertEquals(1_200L.toShortenedStringValue(), "1.2k")
        assertEquals(11_200L.toShortenedStringValue(), "11k")
        assertEquals(1_211_200L.toShortenedStringValue(), "1.2m")
        assertEquals(21_211_200L.toShortenedStringValue(), "21m")
    }
}