package org.mozilla.social.search

interface SearchInteractions {
    fun onQueryTextChanged(text: String) = Unit
    fun onSearchClicked() = Unit
    fun onTabClicked(tab: SearchTab) = Unit
    fun onRetryClicked() = Unit
    fun onFollowClicked(accountId: String, isFollowing: Boolean) = Unit
    fun onAccountClicked(accountId: String) = Unit
    fun onHashTagClicked(name: String) = Unit
    fun onHashTagFollowClicked(name: String, isFollowing: Boolean) = Unit
}