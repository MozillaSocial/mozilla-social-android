package social.firefly.core.analytics

import social.firefly.core.analytics.core.Analytics

class ReportScreenAnalytics internal constructor(private val analytics: Analytics) {

    fun reportScreenViewed() {
        analytics.uiImpression(
            uiIdentifier = REPORT_SCREEN_IMPRESSION,
        )
    }

    companion object {
        private const val REPORT_SCREEN_IMPRESSION = "report.screen.impression"
    }
}