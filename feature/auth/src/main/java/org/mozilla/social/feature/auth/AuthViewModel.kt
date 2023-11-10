package org.mozilla.social.feature.auth

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.mozilla.social.core.analytics.Analytics
import org.mozilla.social.core.analytics.AnalyticsIdentifiers
import org.mozilla.social.core.domain.Login

class AuthViewModel(
    private val analytics: Analytics,
    private val login: Login,
) : ViewModel(), AuthInteractions {

    //TODO remove context
    override fun onSignInClicked(context: Context) {
        viewModelScope.launch {
            login(
                context,
                if (BuildConfig.DEBUG) BuildConfig.stagingUrl else prod
            )
        }
    }

    override fun onChooseServerClicked() {
        super.onChooseServerClicked()
    }

    override fun onScreenViewed() {
        analytics.uiImpression(
            uiIdentifier = AnalyticsIdentifiers.AUTH_SCREEN_IMPRESSION,
        )
    }

    companion object {
        private const val prod = "mozilla.social"
    }
}