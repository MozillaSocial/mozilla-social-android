package org.mozilla.social.post.interactions

interface ContentWarningInteractions {
    fun onContentWarningClicked()
    fun onContentWarningTextChanged(text: String)
}