package social.firefly.core.ui.htmlcontent

interface HtmlContentInteractions {
    fun onLinkClicked(url: String) = Unit

    fun onAccountClicked(accountId: String) = Unit

    fun onHashTagClicked(hashTag: String) = Unit
}
