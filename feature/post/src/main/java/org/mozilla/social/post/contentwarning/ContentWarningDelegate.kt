package org.mozilla.social.post.contentwarning

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.mozilla.social.post.interactions.ContentWarningInteractions

class ContentWarningDelegate : ContentWarningInteractions {

    private val _contentWarningText = MutableStateFlow<String?>(null)
    val contentWarningText = _contentWarningText.asStateFlow()

    override fun onContentWarningClicked() {
        if (contentWarningText.value == null) {
            _contentWarningText.update { "" }
        } else {
            _contentWarningText.update { null }
        }
    }

    override fun onContentWarningTextChanged(text: String) {
        _contentWarningText.update { text }
    }
}