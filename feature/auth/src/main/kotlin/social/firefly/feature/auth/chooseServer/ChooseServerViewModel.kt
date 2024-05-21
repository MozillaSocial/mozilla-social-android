package social.firefly.feature.auth.chooseServer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import social.firefly.common.utils.edit
import social.firefly.core.analytics.ChooseServerAnalytics
import social.firefly.core.usecase.mastodon.auth.Login
import timber.log.Timber

class ChooseServerViewModel(
    private val login: Login,
    private val analytics: ChooseServerAnalytics,
) : ViewModel(), ChooseServerInteractions {
    private val _uiState = MutableStateFlow(ChooseServerUiState())
    val uiState = _uiState.asStateFlow()

    private var servers: List<String> = emptyList()

    override fun onServerTextChanged(text: String) {
        val isUrl = URL_REGEX.toRegex().matches(text)
        val suggestions = if (text.length > 2) {
            servers.filter { it.startsWith(text) }.take(2)
        } else {
            emptyList()
        }
        _uiState.edit {
            copy(
                serverText = text,
                nextButtonEnabled = isUrl,
                suggestedServers = suggestions,
            )
        }
    }

    override fun onNextClicked() {
        _uiState.edit {
            copy(
                isLoading = true,
                loginFailed = false,
            )
        }
        analytics.chooseServerSubmitted(uiState.value.serverText)
        viewModelScope.launch {
            try {
                login(uiState.value.serverText)
            } catch (e: Login.LoginFailedException) {
                _uiState.edit {
                    copy(
                        loginFailed = true,
                    )
                }
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

    override fun onUserCodeReceived(code: String) {
        viewModelScope.launch {
            login.onUserCodeReceived(code)
        }
    }

    override fun onServerListLoaded(servers: List<String>) {
        this.servers = servers
    }

    override fun onServerSuggestionClicked(server: String) {
        onServerTextChanged(server)
    }

    override fun onScreenViewed() {
        analytics.chooseServerScreenViewed()
    }

    companion object {
        @Suppress("MaxLineLength")
        const val URL_REGEX =
            "(https://www\\.|http://www\\.|https://|http://)?[a-zA-Z0-9]+(\\.[a-zA-Z0-9]+)+"
    }
}
