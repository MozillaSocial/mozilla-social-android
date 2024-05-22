package social.firefly.feature.auth.chooseServer

import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
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

    private var servers: List<Server> = emptyList()

    override fun onServerTextChanged(textFieldValue: TextFieldValue) {
        val isUrl = URL_REGEX.toRegex().matches(textFieldValue.text)
        val suggestions = if (textFieldValue.text.length > 2) {
            servers.filter {
                it.name.startsWith(textFieldValue.text) && it.name != textFieldValue.text
            }.sortedByDescending {
                it.monthlyActiveUsers
            }.take(2)
        } else {
            emptyList()
        }
        _uiState.edit {
            copy(
                serverText = textFieldValue,
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
        viewModelScope.launch {
            try {
                login(uiState.value.serverText.text)
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

    override fun onServerListLoaded(servers: List<Server>) {
        this.servers = servers
    }

    override fun onServerSuggestionClicked(server: String) {
        onServerTextChanged(TextFieldValue(text = server, selection = TextRange(server.length)))
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
