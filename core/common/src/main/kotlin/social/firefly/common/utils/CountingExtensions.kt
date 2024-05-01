package social.firefly.common.utils

import java.math.RoundingMode

@Suppress("MagicNumber")
fun Long.toShortenedStringValue(): String? {
    return when {
        this == 0L -> null
        this < 1_000 -> this.toString()
        this < 10_000 -> "${(this / 1_000f).toBigDecimal().setScale(1, RoundingMode.DOWN)}k"
        this < 1_000_000 -> "${(this / 1_000f).toBigDecimal().setScale(0, RoundingMode.DOWN)}k"
        this < 10_000_000 -> "${(this / 1_000_000f).toBigDecimal().setScale(1, RoundingMode.DOWN)}m"
        else -> "${(this / 1_000_000f).toBigDecimal().setScale(0, RoundingMode.DOWN)}m"
    }
}