package social.firefly.feature.auth.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import social.firefly.common.utils.StringFactory
import social.firefly.common.utils.edit
import social.firefly.core.navigation.AuthNavigationDestination
import social.firefly.core.navigation.usecases.NavigateTo
import social.firefly.core.navigation.usecases.ShowSnackbar
import social.firefly.core.usecase.mastodon.auth.Login
import social.firefly.feature.auth.BuildConfig
import social.firefly.feature.auth.R
import timber.log.Timber

class LoginViewModel(
    private val analytics: social.firefly.core.analytics.LoginAnalytics,
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
        private const val PROD = "mastodon.firefly"
    }
}
