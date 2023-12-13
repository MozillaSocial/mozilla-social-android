package org.mozilla.social.core.usecase.mastodon.htmlcontent

import org.mozilla.social.core.navigation.NavigationDestination
import org.mozilla.social.core.navigation.usecases.NavigateTo
import org.mozilla.social.core.navigation.usecases.OpenLink
import org.mozilla.social.core.ui.htmlcontent.HtmlContentInteractions

class DefaultHtmlInteractions(
    private val openLink: OpenLink,
    private val navigateTo: NavigateTo,
) : HtmlContentInteractions {
    override fun onLinkClicked(url: String) = openLink(url)

    override fun onAccountClicked(accountId: String) =
        navigateTo(NavigationDestination.Account(accountId))

    override fun onHashTagClicked(hashTag: String) =
        navigateTo(NavigationDestination.HashTag(hashTag))
}