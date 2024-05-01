package social.firefly.feature.notifications

import social.firefly.common.utils.StringFactory

enum class NotificationsTab(val tabTitle: StringFactory) {
    ALL(StringFactory.resource(R.string.tab_all)),
    MENTIONS(StringFactory.resource(R.string.tab_mentions)),
    REQUESTS(StringFactory.resource(R.string.tab_requests)),
}