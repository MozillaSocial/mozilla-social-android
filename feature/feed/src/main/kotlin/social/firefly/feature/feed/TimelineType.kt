package social.firefly.feature.feed

import social.firefly.common.utils.StringFactory
import social.firefly.feature.feed.R

enum class TimelineType(
    val tabTitle: StringFactory,
) {
    FOR_YOU(StringFactory.resource(R.string.tab_for_you)),
    LOCAL(StringFactory.resource(R.string.tab_local)),
    FEDERATED(StringFactory.resource(R.string.tab_federated)),
}
