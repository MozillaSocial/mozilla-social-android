package org.mozilla.social.post.poll

enum class PollDuration(inSeconds: Int) {
    FIVE_MINUTES(300),
    THIRTY_MINUTES(1_800),
    ONE_HOUR(3_600),
    SIX_HOURS(21_600),
    ONE_DAY(86_400),
    THREE_DAYS(259_200),
    ONE_WEEK(604_800),
}