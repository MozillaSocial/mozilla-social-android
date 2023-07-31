package org.mozilla.social.common.utils

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlin.time.Duration

fun Instant.timeSinceNow(): String {
    val durationSince: Duration = Clock.System.now() - this

    return when {
        durationSince.inWholeSeconds < 60 -> "${durationSince.inWholeSeconds} seconds ago"
        durationSince.inWholeMinutes < 60 -> "${durationSince.inWholeMinutes} minutes ago"
        durationSince.inWholeHours < 24 -> "${durationSince.inWholeHours} hours ago"
        else -> "${durationSince.inWholeDays} days ago"
    }
}