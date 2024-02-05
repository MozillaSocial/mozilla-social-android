package org.mozilla.social.feature.thread

import org.mozilla.social.core.analytics.Analytics

class ThreadAnalytics(private val analytics: Analytics) {
    fun threadScreenViewed() {
        analytics.uiImpression(
            uiIdentifier = THREAD_SCREEN_IMPRESSION,
        )
    }

    companion object {
        private const val THREAD_SCREEN_IMPRESSION = "thread.screen.impression"
    }
}