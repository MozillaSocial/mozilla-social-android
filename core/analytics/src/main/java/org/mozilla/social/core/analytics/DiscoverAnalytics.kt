package org.mozilla.social.core.analytics

import org.mozilla.social.core.analytics.core.Analytics
import org.mozilla.social.core.analytics.core.EngagementType

class DiscoverAnalytics internal constructor(private val analytics: Analytics) {

    fun discoverScreenViewed() {
        analytics.uiImpression(
            uiIdentifier = DISCOVER_SCREEN_IMPRESSION,
        )
    }

    fun recommendationViewed(recommendationId: String) {
        analytics.uiImpression(
            uiIdentifier = DISCOVER_RECOMMENDATION_IMPRESSION,
            recommendationId = recommendationId,
        )
    }

    fun recommendationOpened(recommendationId: String) {
        analytics.uiEngagement(
            engagementType = EngagementType.GENERAL,
            uiIdentifier = DISCOVER_RECOMMENDATION_OPEN,
            recommendationId = recommendationId,
        )
    }

    fun recommendationShared(recommendationId: String) {
        analytics.uiEngagement(
            engagementType = EngagementType.SHARE,
            uiIdentifier = DISCOVER_RECOMMENDATION_SHARE,
            recommendationId = recommendationId,
        )
    }

    companion object {
        private const val DISCOVER_RECOMMENDATION_IMPRESSION = "discover.recommendation.impression"
        private const val DISCOVER_SCREEN_IMPRESSION = "discover.screen.impression"
        private const val DISCOVER_RECOMMENDATION_OPEN = "discover.recommendation.open"
        private const val DISCOVER_RECOMMENDATION_SHARE = "discover.recommendation.share"
    }
}