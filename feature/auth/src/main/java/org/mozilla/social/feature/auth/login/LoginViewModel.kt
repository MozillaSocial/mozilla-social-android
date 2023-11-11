package org.mozilla.social.feature.auth.login

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.mozilla.social.core.analytics.Analytics
import org.mozilla.social.core.analytics.AnalyticsIdentifiers
import org.mozilla.social.core.domain.Login
import org.mozilla.social.core.navigation.AuthNavigationDestination
import org.mozilla.social.core.navigation.usecases.NavigateTo
import org.mozilla.social.feature.auth.BuildConfig

class LoginViewModel(
    private val analytics: Analytics,
    private val login: Login,
    private val navigateTo: NavigateTo,
) : ViewModel(), LoginInteractions {

    //TODO remove context
    //TODO make login rethrow
    override fun onSignInClicked(context: Context) {
        viewModelScope.launch {
            login(
                context,
                if (BuildConfig.DEBUG) BuildConfig.stagingUrl else prod
            )
        }
    }

    override fun onChooseServerClicked() {
        navigateTo(AuthNavigationDestination.ChooseServer)
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