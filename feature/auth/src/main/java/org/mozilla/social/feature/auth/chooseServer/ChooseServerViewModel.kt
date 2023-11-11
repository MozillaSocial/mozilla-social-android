package org.mozilla.social.feature.auth.chooseServer

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.mozilla.social.core.usecase.mastodon.auth.Login

class ChooseServerViewModel(
    private val login: Login,
) : ViewModel(), ChooseServerInteractions {

    private val _uiState = MutableStateFlow(ChooseServerUiState())
    val uiState = _uiState.asStateFlow()

    @Suppress("SwallowedException")
    override fun onServerTextChanged(text: String) {
        val isUrl = URL_REGEX.toRegex().matches(text)
        _uiState.update {
            ChooseServerUiState(
                serverText = text,
                nextButtonEnabled = isUrl,
            )
        }
    }

    //TODO remove context
    //TODO make login rethrow
    override fun onNextClicked(context: Context) {
        viewModelScope.launch {
            login(
                context,
                uiState.value.serverText
            )
        }
    }

    companion object {
        @Suppress("MaxLineLength")
        const val URL_REGEX = "(https://www\\.|http://www\\.|https://|http://)?[a-zA-Z0-9]+(\\.[a-zA-Z0-9]+)(\\.[a-zA-Z0-9]+)?"
    }
}