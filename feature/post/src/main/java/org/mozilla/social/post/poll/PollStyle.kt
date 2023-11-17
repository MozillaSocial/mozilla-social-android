package org.mozilla.social.post.poll

import org.mozilla.social.common.utils.StringFactory
import org.mozilla.social.feature.post.R

enum class PollStyle(
    val label: StringFactory,
) {
    SINGLE_CHOICE(StringFactory.resource(R.string.poll_style_single_choice)),
    MULTIPLE_CHOICE(StringFactory.resource(R.string.poll_style_multiple_choice)),
}
