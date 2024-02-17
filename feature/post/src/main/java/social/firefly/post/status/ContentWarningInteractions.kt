package social.firefly.post.status

interface ContentWarningInteractions {
    fun onContentWarningClicked() = Unit

    fun onContentWarningTextChanged(text: String) = Unit
}
