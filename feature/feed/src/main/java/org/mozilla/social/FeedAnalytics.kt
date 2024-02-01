package org.mozilla.social

import org.mozilla.social.core.analytics.Analytics
import org.mozilla.social.core.analytics.EngagementType
import org.mozilla.social.feed.TimelineType

class FeedAnalytics(private val analytics: Analytics) {

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

    companion object {
        const val FEED_SCREEN_IMPRESSION = "feed.screen.impression"
        const val FEED_HOME_SCREEN_HOME = "feed.screen.home"
        const val FEED_LOCAL_SCREEN_HOME = "feed.screen.local"
        const val FEED_FEDERATED_SCREEN_HOME = "feed.screen.federated"
    }
}