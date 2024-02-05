package org.mozilla.social.feature.auth.chooseServer

import org.mozilla.social.core.analytics.Analytics
import org.mozilla.social.core.analytics.EngagementType

class ChooseServerAnalytics(private val analytics: Analytics) {

    fun chooseServerScreenViewed() {
        analytics.uiImpression(
            uiIdentifier = CHOOSE_A_SERVER_SCREEN_IMPRESSION
        )
    }
    fun chooseServerSubmitted(server: String) {
        analytics.uiEngagement(
            engagementType = EngagementType.GENERAL,
            uiIdentifier = CHOOSE_A_SERVER_SCREEN_SUBMIT_SERVER,
            engagementValue = server,
        )

    }

    companion object {
        const val CHOOSE_A_SERVER_SCREEN_IMPRESSION = "choose.a.server.screen.impression"
        const val CHOOSE_A_SERVER_SCREEN_SUBMIT_SERVER = "choose.a.server.screen.submit-server"
    }
}