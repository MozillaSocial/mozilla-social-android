package social.firefly.core.analytics

import social.firefly.core.analytics.core.Analytics

class ThreadAnalytics internal constructor(private val analytics: Analytics) {
    fun threadScreenViewed() {
        analytics.uiImpression(
            uiIdentifier = THREAD_SCREEN_IMPRESSION,
        )
    }

    companion object {
        private const val THREAD_SCREEN_IMPRESSION = "thread.screen.impression"
    }
}