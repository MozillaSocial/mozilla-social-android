package org.mozilla.social.post.contentwarning

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ContentWarningDelegate(

) : ContentWarningInteractions {

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
        if (text.length > MAX_CONTENT_WARNING_TEXT) return
        _contentWarningText.update { text }
    }

    companion object {
        const val MAX_CONTENT_WARNING_TEXT = 500
    }
}