package org.mozilla.social.feed

import org.mozilla.social.common.utils.StringFactory
import org.mozilla.social.feature.feed.R

enum class TimelineType(
    val tabTitle: StringFactory,
) {
    FOR_YOU(StringFactory.resource(R.string.tab_for_you)),
    LOCAL(StringFactory.resource(R.string.tab_local)),
    FEDERATED(StringFactory.resource(R.string.tab_federated)),
}
