package org.mozilla.social.post.interactions

interface ContentWarningInteractions {
    fun onContentWarningClicked() = Unit
    fun onContentWarningTextChanged(text: String) = Unit
}