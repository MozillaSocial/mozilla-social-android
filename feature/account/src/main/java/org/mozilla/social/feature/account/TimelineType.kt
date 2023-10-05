package org.mozilla.social.feature.account

import org.mozilla.social.common.utils.StringFactory

enum class TimelineType(
    val tabTitle: StringFactory
) {
    POSTS(StringFactory.resource(R.string.tab_posts)),
    POSTS_AND_REPLIES(StringFactory.resource(R.string.tab_posts_and_replies)),
    MEDIA(StringFactory.resource(R.string.tab_media)),
}