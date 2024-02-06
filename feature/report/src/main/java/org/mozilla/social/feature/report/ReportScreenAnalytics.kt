package org.mozilla.social.feature.report

import org.mozilla.social.core.analytics.Analytics

class ReportScreenAnalytics(private val analytics: Analytics) {

    fun reportScreenViewed() {
        analytics.uiImpression(
            uiIdentifier = REPORT_SCREEN_IMPRESSION,
        )
    }

    companion object {
        private const val REPORT_SCREEN_IMPRESSION = "report.screen.impression"
    }
}