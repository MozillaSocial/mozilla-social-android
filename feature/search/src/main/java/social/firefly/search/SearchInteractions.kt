package social.firefly.search

import social.firefly.core.ui.common.following.FollowStatus
import social.firefly.core.ui.common.hashtag.HashtagInteractions
interface SearchInteractions: HashtagInteractions {
    fun onQueryTextChanged(text: String)
    fun onSearchClicked()
    fun onTabClicked(tab: SearchTab)
    fun onRetryClicked()
    fun onFollowClicked(accountId: String, followStatus: FollowStatus)
    fun onAccountClicked(accountId: String)
}

object SearchInteractionsNoOp : SearchInteractions {
    override fun onQueryTextChanged(text: String) = Unit
    override fun onSearchClicked() = Unit
    override fun onTabClicked(tab: SearchTab) = Unit
    override fun onRetryClicked() = Unit
    override fun onFollowClicked(accountId: String, followStatus: FollowStatus) = Unit
    override fun onAccountClicked(accountId: String) = Unit
    override fun onHashTagFollowClicked(name: String, followStatus: FollowStatus) = Unit
    override fun onHashtagClick(name: String) = Unit
}