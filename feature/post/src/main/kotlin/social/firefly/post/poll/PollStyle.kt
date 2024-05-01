package social.firefly.post.poll

import social.firefly.common.utils.StringFactory
import social.firefly.feature.post.R

enum class PollStyle(
    val label: StringFactory,
) {
    SINGLE_CHOICE(StringFactory.resource(R.string.poll_style_single_choice)),
    MULTIPLE_CHOICE(StringFactory.resource(R.string.poll_style_multiple_choice)),
}
