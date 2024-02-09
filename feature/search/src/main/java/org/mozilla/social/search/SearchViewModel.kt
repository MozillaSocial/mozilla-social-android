package org.mozilla.social.search

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
import org.mozilla.social.common.utils.edit
import org.mozilla.social.core.navigation.NavigationDestination
import org.mozilla.social.core.navigation.usecases.NavigateTo
import org.mozilla.social.core.repository.mastodon.SearchRepository
import org.mozilla.social.core.repository.paging.SearchAccountsRemoteMediator
import org.mozilla.social.core.repository.paging.SearchStatusesRemoteMediator
import org.mozilla.social.core.repository.paging.SearchedHashTagsRemoteMediator
import org.mozilla.social.core.ui.accountfollower.toAccountFollowerUiState
import org.mozilla.social.core.ui.common.hashtag.quickview.toHashTagQuickViewUiState
import org.mozilla.social.core.analytics.FeedLocation
import org.mozilla.social.core.analytics.SearchAnalytics
import org.mozilla.social.core.ui.postcard.PostCardDelegate
import org.mozilla.social.core.ui.postcard.toPostCardUiState
import org.mozilla.social.core.usecase.mastodon.account.FollowAccount
import org.mozilla.social.core.usecase.mastodon.account.GetLoggedInUserAccountId
import org.mozilla.social.core.usecase.mastodon.account.UnfollowAccount
import org.mozilla.social.core.usecase.mastodon.hashtag.FollowHashTag
import org.mozilla.social.core.usecase.mastodon.hashtag.UnfollowHashTag
import org.mozilla.social.core.usecase.mastodon.search.SearchAll
import timber.log.Timber

@OptIn(ExperimentalPagingApi::class)
class SearchViewModel(
    private val searchAll: SearchAll,
    getLoggedInUserAccountId: GetLoggedInUserAccountId,
    private val followAccount: FollowAccount,
    private val unfollowAccount: UnfollowAccount,
    private val navigateTo: NavigateTo,
    private val searchRepository: SearchRepository,
    private val followHashTag: FollowHashTag,
    private val unfollowHashTag: UnfollowHashTag,
    private val analytics: SearchAnalytics,
) : ViewModel(), SearchInteractions, KoinComponent {

    private val usersAccountId: String = getLoggedInUserAccountId()

    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState = _uiState.asStateFlow()

    private var searchJob: Job? = null

    val postCardDelegate: PostCardDelegate by inject {
        parametersOf(
            viewModelScope,
            FeedLocation.SEARCH,
        )
    }

    private fun updateAccountFeed() {
        val accountsRemoteMediator = getKoin().inject<SearchAccountsRemoteMediator> {
            parametersOf(
                _uiState.value.query
            )
        }.value

        _uiState.edit { copy(
            accountsFeed = searchRepository.getAccountsPager(
                remoteMediator = accountsRemoteMediator,
            ).map { pagingData ->
                pagingData.map {
                    it.toAccountFollowerUiState(usersAccountId)
                }
            }.cachedIn(viewModelScope)
        ) }
    }

    private fun updateStatusFeed() {
        val statusesRemoteMediator = getKoin().inject<SearchStatusesRemoteMediator> {
            parametersOf(
                _uiState.value.query
            )
        }.value

        _uiState.edit { copy(
            statusFeed = searchRepository.getStatusesPager(
                remoteMediator = statusesRemoteMediator,
            ).map { pagingData ->
                pagingData.map {
                    it.toPostCardUiState(usersAccountId, postCardDelegate)
                }
            }.cachedIn(viewModelScope)
        ) }
    }

    private fun updateHashTagFeed() {
        val hashTagRemoteMediator = getKoin().inject<SearchedHashTagsRemoteMediator> {
            parametersOf(
                _uiState.value.query
            )
        }.value

        _uiState.edit { copy(
            hashTagFeed = searchRepository.getHashTagsPager(
                remoteMediator = hashTagRemoteMediator,
            ).map { pagingData ->
                pagingData.map {
                    it.toHashTagQuickViewUiState()
                }
            }.cachedIn(viewModelScope)
        ) }
    }

    override fun onQueryTextChanged(text: String) {
        _uiState.edit { copy(
            query = text,
        ) }
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
                        it.toPostCardUiState(
                            usersAccountId,
                            postCardDelegate
                        )
                    },
                    accountUiStates = searchResult.accounts.map {
                        it.toSearchedAccountUiState(
                            usersAccountId
                        )
                    },
                )
            }.collect {
                _uiState.edit { copy(
                    topResource = it
                ) }
            }
        }
        analytics.searchClicked(uiState.value.query)
    }

    override fun onRetryClicked() {
        onSearchClicked()
    }

    override fun onTabClicked(tab: SearchTab) {
        _uiState.edit { copy(
            selectedTab = tab
        ) }
        analytics.searchTabClicked(tab.toAnalyticsSearchTab())
    }

    override fun onFollowClicked(accountId: String, isFollowing: Boolean) {
        viewModelScope.launch {
            if (isFollowing) {
                try {
                    unfollowAccount(accountId, usersAccountId)
                } catch (e: UnfollowAccount.UnfollowFailedException) {
                    Timber.e(e)
                }
            } else {
                try {
                    followAccount(accountId, usersAccountId)
                } catch (e: FollowAccount.FollowFailedException) {
                    Timber.e(e)
                }
            }
        }
        analytics.followClicked()
    }

    override fun onAccountClicked(accountId: String) {
        navigateTo(NavigationDestination.Account(accountId = accountId))
        analytics.accountClicked()
    }

    override fun onHashTagClicked(name: String) {
        navigateTo(NavigationDestination.HashTag(name))
        analytics.hashtagClicked()
    }

    override fun onHashTagFollowClicked(name: String, isFollowing: Boolean) {
        viewModelScope.launch {
            if (isFollowing) {
                try {
                    unfollowHashTag(name)
                } catch (e: UnfollowHashTag.UnfollowFailedException) {
                    Timber.e(e)
                }
            } else {
                try {
                    followHashTag(name)
                } catch (e: FollowHashTag.FollowFailedException) {
                    Timber.e(e)
                }
            }
        }
        analytics.hashtagFollowClicked()
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
