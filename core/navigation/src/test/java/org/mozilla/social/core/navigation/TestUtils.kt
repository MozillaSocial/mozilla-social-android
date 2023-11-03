package org.mozilla.social.core.navigation

import kotlin.math.absoluteValue
import kotlin.random.Random

class TestUtils(
    val seed: Long = System.currentTimeMillis(),
    val random: Random = Random(seed),
) {
    init {
        println("random seed: $seed")
    }

    fun randomIdString() = random.nextInt().absoluteValue.toString()

    fun randomWordString(size: Int = random.nextInt(2, 15)) = (1..size)
        .map { ('A'..'Z') + ('a'..'z') + ('0'..'9').random() }
        .joinToString("")
}