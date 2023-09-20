package org.mozilla.social.post.poll

import org.mozilla.social.common.utils.StringFactory
import org.mozilla.social.feature.post.R

enum class PollDuration(
    val inSeconds: Long,
    val label: StringFactory
) {
    FIVE_MINUTES(300, StringFactory.string(R.string.five_minutes)),
    THIRTY_MINUTES(1_800, StringFactory.string(R.string.thirty_minutes)),
    ONE_HOUR(3_600, StringFactory.string(R.string.one_hour)),
    SIX_HOURS(21_600, StringFactory.string(R.string.six_hours)),
    ONE_DAY(86_400, StringFactory.string(R.string.one_day)),
    THREE_DAYS(259_200, StringFactory.string(R.string.three_days)),
    ONE_WEEK(604_800, StringFactory.string(R.string.one_week)),
}