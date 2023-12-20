package org.mozilla.social.search

import org.mozilla.social.common.Resource
import org.mozilla.social.core.model.Account
import org.mozilla.social.core.model.wrappers.DetailedAccountWrapper
import org.mozilla.social.core.ui.common.account.quickview.AccountQuickViewUiState
import org.mozilla.social.core.ui.postcard.PostCardUiState

data class SearchUiState(
    val query: String = "",
    val selectedTab: SearchTab = SearchTab.TOP,
    val topResource: Resource<SearchResultUiState> = Resource.Loading()
)

data class SearchResultUiState(
    val postCardUiStates: List<PostCardUiState>,
    val accountUiStates: List<SearchedAccountUiState>,
)

data class SearchedAccountUiState(
    val quickViewUiState: AccountQuickViewUiState,
    val isFollowing: Boolean,
)

fun DetailedAccountWrapper.toSearchedAccountUiState(): SearchedAccountUiState =
    SearchedAccountUiState(
        quickViewUiState = AccountQuickViewUiState(
            accountId = account.accountId,
            displayName = account.displayName,
            webFinger = account.acct,
            avatarUrl = account.avatarUrl,
        ),
        isFollowing = relationship.isFollowing,
    )
