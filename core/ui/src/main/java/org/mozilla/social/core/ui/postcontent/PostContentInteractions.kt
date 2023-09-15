package org.mozilla.social.core.ui.postcontent

interface PostContentInteractions {
    fun onLinkClicked(url: String) = Unit
    fun onAccountClicked(accountId: String) = Unit
    fun onHashTagClicked(hashTag: String) = Unit
}