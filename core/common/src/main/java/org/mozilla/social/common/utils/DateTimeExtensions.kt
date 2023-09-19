package org.mozilla.social.common.utils

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import org.mozilla.social.core.common.R
import kotlin.time.Duration

fun Instant.timeSinceNow(): StringFactory {
    val durationSince: Duration = Clock.System.now() - this

    return when {
        durationSince.inWholeSeconds < 60 -> StringFactory.quantityString(
            R.plurals.seconds_ago,
            durationSince.inWholeSeconds.toInt()
        )
        durationSince.inWholeMinutes < 60 -> StringFactory.quantityString(
            R.plurals.minutes_ago,
            durationSince.inWholeMinutes.toInt()
        )
        durationSince.inWholeHours < 24 -> StringFactory.quantityString(
            R.plurals.hour_ago,
            durationSince.inWholeHours.toInt()
        )
        else -> StringFactory.quantityString(
            R.plurals.days_ago,
            durationSince.inWholeDays.toInt()
        )
    }
}

fun Instant.timeLeft(): StringFactory? {
    val durationUntil: Duration = this - Clock.System.now()

    return when {
        durationUntil.inWholeSeconds <= 0 -> null
        durationUntil.inWholeSeconds < 60 -> StringFactory.quantityString(
            R.plurals.seconds_left,
            durationUntil.inWholeSeconds.toInt()
        )
        durationUntil.inWholeMinutes < 60 -> StringFactory.quantityString(
            R.plurals.minutes_left,
            durationUntil.inWholeMinutes.toInt()
        )
        durationUntil.inWholeHours < 24 -> StringFactory.quantityString(
            R.plurals.hour_left,
            durationUntil.inWholeHours.toInt()
        )
        else -> StringFactory.quantityString(
            R.plurals.days_left,
            durationUntil.inWholeDays.toInt()
        )
    }
}