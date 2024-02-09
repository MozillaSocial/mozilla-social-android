package org.mozilla.social.core.analytics

import org.mozilla.social.core.analytics.core.Analytics
import org.mozilla.social.core.analytics.core.EngagementType

class FeedAnalytics internal constructor(private val analytics: Analytics) {

    fun feedScreenViewed() {
        analytics.uiImpression(
            uiIdentifier = FEED_SCREEN_IMPRESSION,
        )
    }

    fun feedScreenClicked(timelineType: TimelineType) {
        analytics.uiEngagement(
            engagementType = EngagementType.GENERAL,
            uiIdentifier =
            when (timelineType) {
                TimelineType.FOR_YOU -> FEED_HOME_SCREEN_HOME
                TimelineType.LOCAL -> FEED_LOCAL_SCREEN_HOME
                TimelineType.FEDERATED -> FEED_FEDERATED_SCREEN_HOME
            },
        )
    }

    enum class TimelineType {
        FOR_YOU, LOCAL, FEDERATED
    }

    companion object {
        private const val FEED_SCREEN_IMPRESSION = "feed.screen.impression"
        private const val FEED_HOME_SCREEN_HOME = "feed.screen.home"
        private const val FEED_LOCAL_SCREEN_HOME = "feed.screen.local"
        private const val FEED_FEDERATED_SCREEN_HOME = "feed.screen.federated"
    }
}
