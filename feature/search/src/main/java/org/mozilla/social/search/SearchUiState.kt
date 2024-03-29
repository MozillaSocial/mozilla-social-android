package org.mozilla.social.search

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import org.mozilla.social.common.Resource
import org.mozilla.social.core.model.Account
import org.mozilla.social.core.model.HashTag
import org.mozilla.social.core.model.wrappers.DetailedAccountWrapper
import org.mozilla.social.core.ui.accountfollower.AccountFollowerUiState
import org.mozilla.social.core.ui.common.account.quickview.AccountQuickViewUiState
import org.mozilla.social.core.ui.common.hashtag.quickview.HashTagQuickViewUiState
import org.mozilla.social.core.ui.postcard.PostCardUiState

data class SearchUiState(
    val query: String = "",
    val selectedTab: SearchTab = SearchTab.TOP,
    val topResource: Resource<SearchResultUiState> = Resource.Loading(),
    val accountsFeed: Flow<PagingData<AccountFollowerUiState>>? = null,
    val statusFeed: Flow<PagingData<PostCardUiState>>? = null,
    val hashTagFeed: Flow<PagingData<HashTagQuickViewUiState>>? = null,
)

data class SearchResultUiState(
    val postCardUiStates: List<PostCardUiState>,
    val accountUiStates: List<SearchedAccountUiState>,
)

data class SearchedAccountUiState(
    val quickViewUiState: AccountQuickViewUiState,
    val isFollowing: Boolean,
    val followButtonVisible: Boolean,
)

fun DetailedAccountWrapper.toSearchedAccountUiState(
    currentUserAccountId: String,
): SearchedAccountUiState =
    SearchedAccountUiState(
        quickViewUiState = AccountQuickViewUiState(
            accountId = account.accountId,
            displayName = account.displayName,
            webFinger = account.acct,
            avatarUrl = account.avatarUrl,
        ),
        isFollowing = relationship.isFollowing,
        followButtonVisible = currentUserAccountId != account.accountId,
    )
