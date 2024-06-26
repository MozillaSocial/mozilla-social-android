package social.firefly.feature.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.cachedIn
import androidx.paging.map
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.parameter.parametersOf
import social.firefly.common.utils.edit
import social.firefly.core.analytics.FeedLocation
import social.firefly.core.analytics.SearchAnalytics
import social.firefly.core.navigation.NavigationDestination
import social.firefly.core.navigation.usecases.NavigateTo
import social.firefly.core.repository.paging.pagers.accounts.SearchAccountsPager
import social.firefly.core.repository.paging.pagers.hashTags.SearchHashTagPager
import social.firefly.core.repository.paging.pagers.status.SearchStatusesPager
import social.firefly.core.ui.accountfollower.toAccountFollowerUiState
import social.firefly.core.ui.common.following.FollowStatus
import social.firefly.core.ui.hashtagcard.HashTagCardDelegate
import social.firefly.core.ui.hashtagcard.quickview.toHashTagQuickViewUiState
import social.firefly.core.ui.postcard.PostCardDelegate
import social.firefly.core.ui.postcard.toPostCardUiState
import social.firefly.core.usecase.mastodon.account.FollowAccount
import social.firefly.core.usecase.mastodon.account.GetLoggedInUserAccountId
import social.firefly.core.usecase.mastodon.account.UnfollowAccount
import social.firefly.core.usecase.mastodon.search.SearchAll
import timber.log.Timber

@OptIn(ExperimentalPagingApi::class)
class SearchViewModel(
    private val searchAll: SearchAll,
    getLoggedInUserAccountId: GetLoggedInUserAccountId,
    private val followAccount: FollowAccount,
    private val unfollowAccount: UnfollowAccount,
    private val navigateTo: NavigateTo,
    private val analytics: SearchAnalytics,
) : ViewModel(), SearchInteractions, KoinComponent {

    private val usersAccountId: String = getLoggedInUserAccountId()

    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState = _uiState.asStateFlow()

    private var searchJob: Job? = null

    val postCardDelegate: PostCardDelegate by inject {
        parametersOf(
            FeedLocation.SEARCH,
        )
    }

    val hashTagCardDelegate: HashTagCardDelegate by inject {
        parametersOf(viewModelScope)
    }

    private fun updateAccountFeed() {
        val accountsPager = getKoin().inject<SearchAccountsPager> {
            parametersOf(
                _uiState.value.query
            )
        }.value

        _uiState.edit {
            copy(
                accountsFeed = accountsPager.build().map { pagingData ->
                    pagingData.map {
                        it.toAccountFollowerUiState(usersAccountId)
                    }
                }.cachedIn(viewModelScope)
            )
        }
    }

    private fun updateStatusFeed() {
        val statusesPager = getKoin().inject<SearchStatusesPager> {
            parametersOf(
                _uiState.value.query
            )
        }.value

        _uiState.edit {
            copy(
                statusFeed = statusesPager.build().map { pagingData ->
                    pagingData.map {
                        it.toPostCardUiState(usersAccountId)
                    }
                }.cachedIn(viewModelScope)
            )
        }
    }

    private fun updateHashTagFeed() {
        val hashTagPager = getKoin().inject<SearchHashTagPager> {
            parametersOf(
                _uiState.value.query
            )
        }.value

        _uiState.edit {
            copy(
                hashTagFeed = hashTagPager.build().map { pagingData ->
                    pagingData.map {
                        it.toHashTagQuickViewUiState()
                    }
                }.cachedIn(viewModelScope)
            )
        }
    }

    override fun onQueryTextChanged(text: String) {
        _uiState.edit {
            copy(
                query = text,
            )
        }
    }

    override fun onSearchClicked() {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            updateAccountFeed()
            updateStatusFeed()
            updateHashTagFeed()
            searchAll(
                uiState.value.query,
                viewModelScope,
            ) { searchResult ->
                SearchResultUiState(
                    postCardUiStates = searchResult.statuses.map {
                        it.toPostCardUiState(usersAccountId)
                    },
                    accountUiStates = searchResult.accounts.map {
                        it.toSearchedAccountUiState(usersAccountId)
                    },
                )
            }.collect {
                _uiState.edit {
                    copy(
                        topResource = it
                    )
                }
            }
        }
        analytics.searchClicked(uiState.value.query)
    }

    override fun onRetryClicked() {
        onSearchClicked()
    }

    override fun onTabClicked(tab: SearchTab) {
        _uiState.edit {
            copy(
                selectedTab = tab
            )
        }
        analytics.searchTabClicked(tab.toAnalyticsSearchTab())
    }

    override fun onFollowClicked(accountId: String, followStatus: FollowStatus) {
        viewModelScope.launch {
            when (followStatus) {
                FollowStatus.FOLLOWING,
                FollowStatus.PENDING_REQUEST -> {
                    try {
                        unfollowAccount(accountId, usersAccountId)
                    } catch (e: UnfollowAccount.UnfollowFailedException) {
                        Timber.e(e)
                    }
                }
                FollowStatus.NOT_FOLLOWING -> {
                    try {
                        followAccount(accountId, usersAccountId)
                    } catch (e: FollowAccount.FollowFailedException) {
                        Timber.e(e)
                    }
                }
            }
        }
        analytics.followClicked()
    }

    override fun onAccountClicked(accountId: String) {
        navigateTo(NavigationDestination.Account(accountId = accountId))
        analytics.accountClicked()
    }
}

private fun SearchTab.toAnalyticsSearchTab(): SearchAnalytics.SearchTab {
    return when (this) {
        SearchTab.TOP -> SearchAnalytics.SearchTab.TOP
        SearchTab.ACCOUNTS -> SearchAnalytics.SearchTab.ACCOUNTS
        SearchTab.POSTS -> SearchAnalytics.SearchTab.POSTS
        SearchTab.HASHTAGS -> SearchAnalytics.SearchTab.HASHTAGS
    }
}
