package org.mozilla.social.feature.hashtag

import org.mozilla.social.core.analytics.Analytics
import org.mozilla.social.core.analytics.EngagementType

class HashtagAnalytics(private val analytics: Analytics) {
    fun hashtagScreenViewed() {
        analytics.uiImpression(
            uiIdentifier = HASHTAG_SCREEN_IMPRESSION,
        )
    }

    fun followClicked() {
        analytics.uiEngagement(
            engagementType = EngagementType.GENERAL,
            uiIdentifier = HASHTAG_FOLLOW,
        )
    }

    companion object {
        private const val HASHTAG_SCREEN_IMPRESSION = "hashtag.screen.impression"
        private const val HASHTAG_FOLLOW = "hashtag.follow"
    }
}