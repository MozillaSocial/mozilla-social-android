package social.firefly.feature.auth.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import social.firefly.common.utils.StringFactory
import social.firefly.common.utils.edit
import social.firefly.core.analytics.LoginAnalytics
import social.firefly.core.navigation.AuthNavigationDestination
import social.firefly.core.navigation.usecases.NavigateTo
import social.firefly.core.navigation.usecases.ShowSnackbar
import social.firefly.core.usecase.mastodon.auth.Login
import social.firefly.feature.auth.R
import timber.log.Timber

class LoginViewModel(
    private val login: Login,
    private val analytics: LoginAnalytics,
    private val navigateTo: NavigateTo,
    private val showSnackbar: ShowSnackbar,
) : ViewModel(), LoginInteractions {
    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState = _uiState.asStateFlow()

    override fun onSignInClicked() {
        signIn(PROD)
    }

    override fun onChooseServerClicked() {
        analytics.chooseAServerClicked()
        navigateTo(AuthNavigationDestination.ChooseServer)
    }

    override fun onSignInToStagingClicked() {
        signIn(STAGING)
    }

    private fun signIn(url: String) {
        _uiState.edit {
            copy(
                isLoading = true,
            )
        }
        analytics.signInSignUpClicked()
        viewModelScope.launch {
            try {
                login(url)
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

    override fun onScreenViewed() {
        analytics.loginScreenViewed()
    }

    companion object {
        private const val PROD = "mozilla.social"
        private const val STAGING = "stage.moztodon.nonprod.webservices.mozgcp.net"
    }
}
