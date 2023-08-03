package org.mozilla.social.common.utils

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlin.time.Duration

fun Instant.timeSinceNow(): String {
    val durationSince: Duration = Clock.System.now() - this

    return when {
        durationSince.inWholeSeconds == 1L -> "${durationSince.inWholeSeconds} second ago"
        durationSince.inWholeSeconds < 60 -> "${durationSince.inWholeSeconds} seconds ago"
        durationSince.inWholeMinutes == 1L -> "${durationSince.inWholeMinutes} minute ago"
        durationSince.inWholeMinutes < 60 -> "${durationSince.inWholeMinutes} minutes ago"
        durationSince.inWholeHours == 1L -> "${durationSince.inWholeHours} hour ago"
        durationSince.inWholeHours < 24 -> "${durationSince.inWholeHours} hours ago"
        durationSince.inWholeDays == 1L -> "${durationSince.inWholeDays} day ago"
        else -> "${durationSince.inWholeDays} days ago"
    }
}

fun Instant.timeLeft(): String? {
    val durationUntil: Duration = this - Clock.System.now()

    return when {
        durationUntil.inWholeSeconds <= 0 -> null
        durationUntil.inWholeSeconds == 1L -> "${durationUntil.inWholeSeconds} second left"
        durationUntil.inWholeSeconds < 60 -> "${durationUntil.inWholeSeconds} seconds left"
        durationUntil.inWholeMinutes == 1L -> "${durationUntil.inWholeMinutes} minute left"
        durationUntil.inWholeMinutes < 60 -> "${durationUntil.inWholeMinutes} minutes left"
        durationUntil.inWholeHours == 1L -> "${durationUntil.inWholeHours} hour left"
        durationUntil.inWholeHours < 24 -> "${durationUntil.inWholeHours} hours left"
        durationUntil.inWholeDays == 1L -> "${durationUntil.inWholeDays} day left"
        else -> "${durationUntil.inWholeDays} days left"
    }
}