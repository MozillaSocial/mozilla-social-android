package org.mozilla.social.post.contentwarning

interface ContentWarningInteractions {
    fun onContentWarningClicked() = Unit
    fun onContentWarningTextChanged(text: String) = Unit
}