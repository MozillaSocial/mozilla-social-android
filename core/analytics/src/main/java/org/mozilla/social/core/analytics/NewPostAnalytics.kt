package org.mozilla.social.core.analytics

import org.mozilla.social.core.analytics.core.Analytics
import org.mozilla.social.core.analytics.core.EngagementType

class NewPostAnalytics internal constructor(private val analytics: Analytics) {
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
        private const val NEW_POST_SCREEN_IMPRESSION = "new.post.screen.impression"
        private const val NEW_POST_POST = "new.post.post"
        private const val NEW_POST_IMAGE = "new.post.image"
        private const val NEW_POST_MEDIA = "new.post.media"
        private const val NEW_POST_POLL = "new.post.poll"
        private const val NEW_POST_CONTENT_WARNING = "new.post.content.warning"
    }
}