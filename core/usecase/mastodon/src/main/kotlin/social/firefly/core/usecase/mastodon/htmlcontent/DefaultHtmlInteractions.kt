package social.firefly.core.usecase.mastodon.htmlcontent

import social.firefly.core.navigation.NavigationDestination
import social.firefly.core.navigation.usecases.NavigateTo
import social.firefly.core.navigation.usecases.OpenLink
import social.firefly.core.ui.htmlcontent.HtmlContentInteractions

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