package social.firefly.feature.auth.login

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import social.firefly.core.analytics.LoginAnalytics
import social.firefly.core.navigation.AuthNavigationDestination
import social.firefly.core.navigation.usecases.NavigateTo

class LoginViewModel(
    private val analytics: LoginAnalytics,
    private val navigateTo: NavigateTo,
) : ViewModel(), LoginInteractions {
    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState = _uiState.asStateFlow()

    override fun onChooseServerClicked() {
        analytics.chooseAServerClicked()
        navigateTo(AuthNavigationDestination.ChooseServer)
    }

    override fun onScreenViewed() {
        analytics.loginScreenViewed()
    }
}
