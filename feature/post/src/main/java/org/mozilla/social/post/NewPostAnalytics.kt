package org.mozilla.social.post

import org.mozilla.social.core.analytics.Analytics
import org.mozilla.social.core.analytics.AnalyticsIdentifiers
import org.mozilla.social.core.analytics.EngagementType

class NewPostAnalytics(private val analytics: Analytics) {
    fun postClicked() {
        analytics.uiEngagement(
            engagementType = EngagementType.POST,
            uiIdentifier = NEW_POST_POST,
        )
    }

    fun newPostScreenViewed() {
        analytics.uiImpression(
            uiIdentifier = NEW_POST_SCREEN_IMPRESSION,
        )
    }

    fun uploadMediaClicked() {
        analytics.uiEngagement(
            engagementType = EngagementType.POST,
            uiIdentifier = NEW_POST_MEDIA
        )
    }

    fun uploadImageClicked() {
        analytics.uiEngagement(
            engagementType = EngagementType.POST,
            uiIdentifier = NEW_POST_IMAGE
        )
    }

    fun newPollClicked() {
        analytics.uiEngagement(
            engagementType = EngagementType.POST,
            uiIdentifier = NEW_POST_POLL
        )
    }

    fun contentWarningClicked() {
        analytics.uiEngagement(
            engagementType = EngagementType.POST,
            uiIdentifier = NEW_POST_CONTENT_WARNING
        )
    }

    companion object {
        const val NEW_POST_SCREEN_IMPRESSION = "new.post.screen.impression"
        const val NEW_POST_POST = "new.post.post"
        const val NEW_POST_IMAGE = "new.post.image"
        const val NEW_POST_MEDIA = "new.post.media"
        const val NEW_POST_POLL = "new.post.poll"
        const val NEW_POST_CONTENT_WARNING = "new.post.content.warning"
    }
}