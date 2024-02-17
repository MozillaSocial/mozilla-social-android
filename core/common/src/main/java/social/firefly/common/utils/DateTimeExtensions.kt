package social.firefly.common.utils

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import social.firefly.core.common.R
import kotlin.time.Duration

@Suppress("MagicNumber")

fun Instant.timeSinceNow(): StringFactory {
    val durationSince: Duration = Clock.System.now() - this

    return when {
        durationSince.inWholeSeconds < 60 ->
            StringFactory.resource(R.string.just_now)
        durationSince.inWholeMinutes < 60 ->
            StringFactory.quantityResource(
                R.plurals.minutes_ago,
                durationSince.inWholeMinutes.toInt(),
                durationSince.inWholeMinutes.toInt(),
            )
        durationSince.inWholeHours < 24 ->
            StringFactory.quantityResource(
                R.plurals.hour_ago,
                durationSince.inWholeHours.toInt(),
                durationSince.inWholeHours.toInt(),
            )
        else ->
            StringFactory.quantityResource(
                R.plurals.days_ago,
                durationSince.inWholeDays.toInt(),
                durationSince.inWholeDays.toInt(),
            )
    }
}

@Suppress("MagicNumber")
fun Instant.timeLeft(): StringFactory? {
    val durationUntil: Duration = this - Clock.System.now()

    return when {
        durationUntil.inWholeSeconds <= 0 -> null
        durationUntil.inWholeSeconds < 60 ->
            StringFactory.quantityResource(
                R.plurals.seconds_left,
                durationUntil.inWholeSeconds.toInt(),
                durationUntil.inWholeSeconds.toInt(),
            )
        durationUntil.inWholeMinutes < 60 ->
            StringFactory.quantityResource(
                R.plurals.minutes_left,
                durationUntil.inWholeMinutes.toInt(),
                durationUntil.inWholeMinutes.toInt(),
            )
        durationUntil.inWholeHours < 24 ->
            StringFactory.quantityResource(
                R.plurals.hour_left,
                durationUntil.inWholeHours.toInt(),
                durationUntil.inWholeHours.toInt(),
            )
        else ->
            StringFactory.quantityResource(
                R.plurals.days_left,
                durationUntil.inWholeDays.toInt(),
                durationUntil.inWholeDays.toInt(),
            )
    }
}
