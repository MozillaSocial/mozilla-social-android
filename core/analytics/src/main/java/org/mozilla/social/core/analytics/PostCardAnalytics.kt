package org.mozilla.social.core.analytics

import org.mozilla.social.core.analytics.core.Analytics
import org.mozilla.social.core.analytics.core.EngagementType

class PostCardAnalytics internal constructor(private val analytics: Analytics) {

    fun replyClicked(baseAnalyticsIdentifier: String) {
        analytics.uiEngagement(
            engagementType = EngagementType.GENERAL,
            uiIdentifier = "$baseAnalyticsIdentifier.$FEED_POST_REPLY",
        )
    }

    fun boostClicked(baseAnalyticsIdentifier: String) {
        analytics.uiEngagement(
            engagementType = EngagementType.GENERAL,
            uiIdentifier = "$baseAnalyticsIdentifier.$FEED_POST_BOOST",
        )
    }

    fun unboostClicked(baseAnalyticsIdentifier: String) {
        analytics.uiEngagement(
            engagementType = EngagementType.GENERAL,
            uiIdentifier = "$baseAnalyticsIdentifier.$FEED_POST_UNBOOST",
        )
    }

    fun favoriteClicked(baseAnalyticsIdentifier: String) {
        analytics.uiEngagement(
            engagementType = EngagementType.FAVORITE,
            uiIdentifier = "$baseAnalyticsIdentifier.$FEED_POST_FAVORITE",
        )
    }

    fun unfavoriteClicked(baseAnalyticsIdentifier: String) {
        analytics.uiEngagement(
            engagementType = EngagementType.FAVORITE,
            uiIdentifier = "$baseAnalyticsIdentifier.$FEED_POST_UNFAVORITE",
        )

    }

    fun muteClicked(baseAnalyticsIdentifier: String) {
        analytics.uiEngagement(
            engagementType = EngagementType.GENERAL,
            uiIdentifier = "$baseAnalyticsIdentifier.$FEED_POST_MUTE",
        )
    }

    fun blockClicked(baseAnalyticsIdentifier: String) {
        analytics.uiEngagement(
            engagementType = EngagementType.GENERAL,
            uiIdentifier = "$baseAnalyticsIdentifier.$FEED_POST_BLOCK",
        )
    }

    fun reportClicked(baseAnalyticsIdentifier: String) {
        analytics.uiEngagement(
            engagementType = EngagementType.GENERAL,
            uiIdentifier = "$baseAnalyticsIdentifier.$FEED_POST_REPORT",
        )

    }

    fun accountImageClicked(baseAnalyticsIdentifier: String) {
        analytics.uiEngagement(
            engagementType = EngagementType.GENERAL,
            uiIdentifier = "$baseAnalyticsIdentifier.$FEED_POST_ACCOUNT_IMAGE_TAPPED",
        )
    }

    fun accountClicked() {
        analytics.uiEngagement(
            engagementType = EngagementType.GENERAL,
            uiIdentifier = FEED_POST_ACCOUNT_TAPPED,
        )

    }

    fun onLinkClicked() {
        analytics.uiEngagement(
            engagementType = EngagementType.GENERAL,
            uiIdentifier = FEED_POST_LINK_TAPPED,
        )
    }

    fun hashtagClicked() {
        analytics.uiEngagement(
            engagementType = EngagementType.GENERAL,
            uiIdentifier = FEED_POST_HASHTAG_TAPPED,
        )
    }

    companion object {
        private const val FEED_POST_REPLY = "feed.post.open-reply"
        private const val FEED_POST_BOOST = "feed.post.reblog"
        private const val FEED_POST_UNBOOST = "feed.post.unreblog"
        private const val FEED_POST_FAVORITE = "feed.post.favourite"
        private const val FEED_POST_UNFAVORITE = "feed.post.unfavourite"
        private const val FEED_POST_LINK_TAPPED = "post.link.tap"
        private const val FEED_POST_ACCOUNT_TAPPED = "post.account.tap"
        private const val FEED_POST_ACCOUNT_IMAGE_TAPPED = "post.account.image.tap"
        private const val FEED_POST_HASHTAG_TAPPED = "post.hashtag.tap"
        private const val FEED_POST_MUTE = "feed.post.mute"
        private const val FEED_POST_BLOCK = "feed.post.block"
        private const val FEED_POST_REPORT = "feed.post.report"
    }
}

enum class FeedLocation(val baseAnalyticsIdentifier: String) {
    PROFILE("profile"),
    HOME("home"),
    LOCAL("local"),
    FEDERATED("federated"),
    HASHTAG("hashtag"),
    THREAD("thread"),
    FAVORITES("favorites"),
    SEARCH("search"),
    NOTIFICATIONS("notifications"),
}