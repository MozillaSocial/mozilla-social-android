package org.mozilla.social.feature.auth.login

import org.mozilla.social.core.analytics.Analytics
import org.mozilla.social.core.analytics.EngagementType

class LoginAnalytics(private val analytics: Analytics) {

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

        const val AUTH_SCREEN_IMPRESSION = "auth.screen.impression"
        const val AUTH_SCREEN_SIGN_IN_SIGN_UP = "auth.screen.sign-in-sign-up"
        const val AUTH_SCREEN_CHOOSE_A_SERVER = "auth.screen.choose-a-server"
    }
}