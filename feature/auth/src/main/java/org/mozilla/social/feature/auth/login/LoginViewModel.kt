package org.mozilla.social.feature.auth.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.mozilla.social.common.utils.StringFactory
import org.mozilla.social.common.utils.edit
import org.mozilla.social.core.navigation.AuthNavigationDestination
import org.mozilla.social.core.navigation.usecases.NavigateTo
import org.mozilla.social.core.navigation.usecases.ShowSnackbar
import org.mozilla.social.core.usecase.mastodon.auth.Login
import org.mozilla.social.feature.auth.BuildConfig
import org.mozilla.social.feature.auth.R
import timber.log.Timber

class LoginViewModel(
    private val analytics: org.mozilla.social.core.analytics.LoginAnalytics,
    private val login: Login,
    private val navigateTo: NavigateTo,
    private val showSnackbar: ShowSnackbar,
) : ViewModel(), LoginInteractions {
    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState = _uiState.asStateFlow()

    override fun onSignInClicked() {
        _uiState.edit {
            copy(
                isLoading = true,
            )
        }
        analytics.signInSignUpClicked()
        viewModelScope.launch {
            try {
                login(PROD)
            } catch (e: Login.LoginFailedException) {
                showSnackbar(
                    text = StringFactory.resource(R.string.error_connecting),
                    isError = true,
                )
                Timber.e(e)
            } finally {
                _uiState.edit {
                    copy(
                        isLoading = false,
                    )
                }
            }
        }
    }

    override fun onChooseServerClicked() {
        analytics.chooseAServerClicked()
        navigateTo(AuthNavigationDestination.ChooseServer)
    }

    override fun onScreenViewed() {
        analytics.loginScreenViewed()
    }

    companion object {
        private const val PROD = "mastodon.social"
    }
}
