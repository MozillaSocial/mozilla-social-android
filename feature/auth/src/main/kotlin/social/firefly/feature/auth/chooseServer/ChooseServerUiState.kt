package social.firefly.feature.auth.chooseServer

import androidx.compose.ui.text.input.TextFieldValue

data class ChooseServerUiState(
    val serverText: TextFieldValue = TextFieldValue(),
    val nextButtonEnabled: Boolean = false,
    val loginFailed: Boolean = false,
    val isLoading: Boolean = false,
    val suggestedServers: List<Server> = emptyList(),
)
