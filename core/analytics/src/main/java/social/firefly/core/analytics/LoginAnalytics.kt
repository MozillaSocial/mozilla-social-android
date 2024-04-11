package social.firefly.core.analytics

import social.firefly.core.analytics.core.Analytics
import social.firefly.core.analytics.core.EngagementType

class LoginAnalytics internal constructor(private val analytics: Analytics) {

    fun loginScreenViewed() {
        analytics.uiImpression(
            uiIdentifier = AUTH_SCREEN_IMPRESSION,
        )
    }

    fun signInSignUpClicked() {
        analytics.uiEngagement(
            engagementType = EngagementType.GENERAL,
            uiIdentifier = AUTH_SCREEN_SIGN_IN_SIGN_UP,
        )
    }

    fun chooseAServerClicked() {
        analytics.uiEngagement(
            engagementType = EngagementType.GENERAL,
            uiIdentifier = AUTH_SCREEN_CHOOSE_A_SERVER,
        )
    }

    companion object {
        private const val AUTH_SCREEN_IMPRESSION = "auth.screen.impression"
        private const val AUTH_SCREEN_SIGN_IN_SIGN_UP = "auth.screen.sign-in-sign-up"
        private const val AUTH_SCREEN_CHOOSE_A_SERVER = "auth.screen.choose-a-server"
    }
}