package social.firefly.search

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import social.firefly.common.Resource
import social.firefly.core.model.wrappers.DetailedAccountWrapper
import social.firefly.core.ui.accountfollower.AccountFollowerUiState
import social.firefly.core.ui.common.account.quickview.AccountQuickViewUiState
import social.firefly.core.ui.common.following.FollowStatus
import social.firefly.core.ui.common.hashtag.quickview.HashTagQuickViewUiState
import social.firefly.core.ui.postcard.PostCardUiState

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
    val followStatus: FollowStatus,
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
        followStatus = when {
            relationship.hasPendingFollowRequest -> FollowStatus.PENDING_REQUEST
            relationship.isFollowing -> FollowStatus.FOLLOWING
            else -> FollowStatus.NOT_FOLLOWING
        },
        followButtonVisible = currentUserAccountId != account.accountId,
    )
