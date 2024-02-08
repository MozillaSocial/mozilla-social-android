package org.mozilla.social.core.analytics

import org.mozilla.social.core.analytics.core.Analytics
import org.mozilla.social.core.analytics.core.EngagementType

class HashtagAnalytics internal constructor(private val analytics: Analytics) {
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