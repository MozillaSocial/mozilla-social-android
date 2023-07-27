package org.mozilla.social.post.poll

enum class PollDuration(
    val inSeconds: Long,
    val label: String
) {
    FIVE_MINUTES(300, "5 minutes"),
    THIRTY_MINUTES(1_800, "30 minutes"),
    ONE_HOUR(3_600, "1 hour"),
    SIX_HOURS(21_600, "6 hours"),
    ONE_DAY(86_400, "1 day"),
    THREE_DAYS(259_200, "3 days"),
    ONE_WEEK(604_800, "1 week"),
}