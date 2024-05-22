package social.firefly.feature.auth.chooseServer

import androidx.compose.ui.text.input.TextFieldValue

internal interface ChooseServerInteractions {
    fun onServerTextChanged(textFieldValue: TextFieldValue) = Unit
    fun onNextClicked() = Unit
    fun onScreenViewed() = Unit
    fun onUserCodeReceived(code: String) = Unit
    fun onServerListLoaded(servers: List<Server>) = Unit
    fun onServerSuggestionClicked(server: String) = Unit
}
