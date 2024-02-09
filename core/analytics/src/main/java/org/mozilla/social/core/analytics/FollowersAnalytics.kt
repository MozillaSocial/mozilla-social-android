package org.mozilla.social.core.analytics

import org.mozilla.social.core.analytics.core.Analytics
import org.mozilla.social.core.analytics.core.EngagementType

class FollowersAnalytics internal constructor(private val analytics: Analytics) {

    fun followersScreenViewed() {
        analytics.uiImpression(
            uiIdentifier = FOLLOWERS_SCREEN_IMPRESSION,
        )
    }

    fun followingScreenViewed() {
        analytics.uiImpression(
            uiIdentifier = FOLLOWING_SCREEN_IMPRESSION,
        )
    }

    fun followClicked() {
        analytics.uiEngagement(
            engagementType = EngagementType.GENERAL,
            uiIdentifier = FOLLOWS_SCREEN_FOLLOW,
        )
    }

    fun unfollowClicked() {
        analytics.uiEngagement(
            engagementType = EngagementType.GENERAL,
            uiIdentifier = FOLLOWS_SCREEN_UNFOLLOW,
        )
    }

    companion object {
        private const val FOLLOWERS_SCREEN_IMPRESSION = "followers.screen.impression"
        private const val FOLLOWING_SCREEN_IMPRESSION = "following.screen.impression"
        private const val FOLLOWS_SCREEN_FOLLOW = "follows.screen.follow"
        private const val FOLLOWS_SCREEN_UNFOLLOW = "follows.screen.unfollow"
    }
}