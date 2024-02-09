package org.mozilla.social.core.analytics

import org.mozilla.social.core.analytics.core.Analytics

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