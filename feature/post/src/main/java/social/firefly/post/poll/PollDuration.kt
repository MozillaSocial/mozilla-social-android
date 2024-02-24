package social.firefly.post.poll

import social.firefly.common.utils.StringFactory
import social.firefly.feature.post.R

@Suppress("MagicNumber")
enum class PollDuration(
    val inSeconds: Long,
    val label: StringFactory,
) {
    FIVE_MINUTES(300, StringFactory.resource(R.string.five_minutes)),
    THIRTY_MINUTES(1_800, StringFactory.resource(R.string.thirty_minutes)),
    ONE_HOUR(3_600, StringFactory.resource(R.string.one_hour)),
    SIX_HOURS(21_600, StringFactory.resource(R.string.six_hours)),
    TWELVE_HOURS(43_200, StringFactory.resource(R.string.twelve_hours)),
    ONE_DAY(86_400, StringFactory.resource(R.string.one_day)),
    THREE_DAYS(259_200, StringFactory.resource(R.string.three_days)),
    ONE_WEEK(604_800, StringFactory.resource(R.string.one_week)),
}
