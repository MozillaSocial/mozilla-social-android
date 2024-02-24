package social.firefly.search

import social.firefly.core.ui.common.following.FollowStatus

interface SearchInteractions {
    fun onQueryTextChanged(text: String) = Unit
    fun onSearchClicked() = Unit
    fun onTabClicked(tab: SearchTab) = Unit
    fun onRetryClicked() = Unit
    fun onFollowClicked(accountId: String, followStatus: FollowStatus) = Unit
    fun onAccountClicked(accountId: String) = Unit
    fun onHashTagClicked(name: String) = Unit
    fun onHashTagFollowClicked(name: String, followStatus: FollowStatus) = Unit
}