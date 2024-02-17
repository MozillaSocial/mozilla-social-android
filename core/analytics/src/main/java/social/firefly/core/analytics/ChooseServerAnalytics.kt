package social.firefly.core.analytics

import social.firefly.core.analytics.core.Analytics
import social.firefly.core.analytics.core.EngagementType

class ChooseServerAnalytics internal constructor(private val analytics: Analytics) {

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