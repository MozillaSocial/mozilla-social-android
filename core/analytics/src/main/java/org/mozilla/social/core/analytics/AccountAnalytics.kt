package org.mozilla.social.core.analytics

import org.mozilla.social.core.analytics.core.Analytics
import org.mozilla.social.core.analytics.core.EngagementType

class AccountAnalytics internal constructor(private val analytics: Analytics) {

    fun accountScreenViewed() {
        analytics.uiImpression(
            uiIdentifier = ACCOUNTS_SCREEN_IMPRESSION,
        )
    }

    fun overflowShareClicked() {
        analytics.uiEngagement(
            uiIdentifier = PROFILE_OVERFLOW_SHARE,
        )
    }

    fun overflowMuteClicked() {
        analytics.uiEngagement(
            engagementType = EngagementType.GENERAL,
            uiIdentifier = PROFILE_OVERFLOW_MUTE,
        )
    }

    fun overflowUnmuteClicked() {
        analytics.uiEngagement(
            engagementType = EngagementType.GENERAL,
            uiIdentifier = PROFILE_OVERFLOW_UNMUTE,
        )
    }

    fun overflowBlockClicked() {
        analytics.uiEngagement(
            engagementType = EngagementType.GENERAL,
            uiIdentifier = PROFILE_OVERFLOW_BLOCK,
        )
    }

    fun overflowUnblockClicked() {
        analytics.uiEngagement(
            engagementType = EngagementType.GENERAL,
            uiIdentifier = PROFILE_OVERFLOW_UNBLOCK,
        )
    }

    fun overflowReportClicked() {
        analytics.uiEngagement(
            engagementType = EngagementType.GENERAL,
            uiIdentifier = PROFILE_OVERFLOW_REPORT,
        )
    }

    fun followClicked() {
        analytics.uiEngagement(
            uiIdentifier = ACCOUNTS_SCREEN_FOLLOW,
        )
    }

    fun unfollowClicked() {
        analytics.uiEngagement(
            uiIdentifier = ACCOUNTS_SCREEN_UNFOLLOW,
        )
    }

    fun tabClicked(timelineType: TimelineType) {
        analytics.uiEngagement(uiIdentifier = timelineType.identifier)
    }

    fun editAccountClicked() {
        analytics.uiEngagement(
            uiIdentifier = PROFILE_EDIT_PROFILE,
        )
    }

    fun editAccountSaved() {
        analytics.uiEngagement(
            uiIdentifier = PROFILE_EDIT_PROFILE_SAVE,
        )
    }

    fun editAccountScreenViewed() {
        analytics.uiImpression(
            uiIdentifier = PROFILE_EDIT_PROFILE_SCREEN_IMPRESSION,
        )
    }

    enum class TimelineType(val identifier: String) {
        POSTS(PROFILE_FEED_POSTS), POSTS_AND_REPLIES(PROFILE_FEED_POSTS_AND_REPLIES), MEDIA(
            PROFILE_FEED_MEDIA
        )
    }

    companion object {
        private const val ACCOUNTS_SCREEN_IMPRESSION = "account.screen.impression"
        private const val ACCOUNTS_SCREEN_FOLLOW = "profile.follow_btn.follow"
        private const val ACCOUNTS_SCREEN_UNFOLLOW = "profile.follow_btn.unfollow"
        private const val PROFILE_EDIT_PROFILE = "profile.edit.profile"
        private const val PROFILE_EDIT_PROFILE_SCREEN_IMPRESSION =
            "profile.edit.profile.screen.impression"
        private const val PROFILE_EDIT_PROFILE_SAVE = "profile.edit.profile.save"
        private const val PROFILE_OVERFLOW_SHARE = "profile.more.share-account"
        private const val PROFILE_OVERFLOW_MUTE = "profile.more.mute"
        private const val PROFILE_OVERFLOW_UNMUTE = "profile.more.unmute"
        private const val PROFILE_OVERFLOW_BLOCK = "profile.more.block"
        private const val PROFILE_OVERFLOW_UNBLOCK = "profile.more.unblock"
        private const val PROFILE_OVERFLOW_REPORT = "profile.more.report.open"
        private const val PROFILE_FEED_POSTS = "profile.tabs.posts"
        private const val PROFILE_FEED_POSTS_AND_REPLIES = "profile.tabs.posts-and-replies"
        private const val PROFILE_FEED_MEDIA = "profile.tabs.media"
    }
}