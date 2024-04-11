package social.firefly.core.test

import kotlin.math.absoluteValue
import kotlin.random.Random

class TestUtils(
    private val seed: Long = System.currentTimeMillis(),
    private val random: Random = Random(seed),
) {
    private val charRange = ('A'..'Z') + ('a'..'z') + ('0'..'9')

    init {
        println("random seed: $seed")
    }

    fun randomIdString() = random.nextInt().absoluteValue.toString()

    fun randomWordString(size: Int = random.nextInt(2, 15)) =
        (1..size)
            .map { charRange.random() }
            .joinToString(separator = "")
}

fun main() {
    println(TestUtils().randomWordString())
}
