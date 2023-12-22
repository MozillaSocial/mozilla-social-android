package org.mozilla.social.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.Koin
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.parameter.parametersOf
import org.mozilla.social.common.utils.edit
import org.mozilla.social.core.analytics.AnalyticsIdentifiers
import org.mozilla.social.core.navigation.NavigationDestination
import org.mozilla.social.core.navigation.usecases.NavigateTo
import org.mozilla.social.core.repository.mastodon.SearchRepository
import org.mozilla.social.core.repository.paging.SearchAccountsRemoteMediator
import org.mozilla.social.core.repository.paging.SearchStatusesRemoteMediator
import org.mozilla.social.core.ui.accountfollower.AccountFollowerUiState
import org.mozilla.social.core.ui.accountfollower.toAccountFollowerUiState
import org.mozilla.social.core.ui.postcard.PostCardDelegate
import org.mozilla.social.core.ui.postcard.toPostCardUiState
import org.mozilla.social.core.usecase.mastodon.account.FollowAccount
import org.mozilla.social.core.usecase.mastodon.account.GetLoggedInUserAccountId
import org.mozilla.social.core.usecase.mastodon.account.UnfollowAccount
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
) : ViewModel(), SearchInteractions, KoinComponent {

    private val usersAccountId: String = getLoggedInUserAccountId()

    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState = _uiState.asStateFlow()

    private var searchJob: Job? = null

    val postCardDelegate: PostCardDelegate by inject {
        parametersOf(
            viewModelScope,
            AnalyticsIdentifiers.FEED_PREFIX_SEARCH,
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
                    it.toAccountFollowerUiState()
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
                    it.toPostCardUiState(usersAccountId)
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
            searchAll(
                uiState.value.query,
                viewModelScope,
            ) { searchResult ->
                SearchResultUiState(
                    postCardUiStates = searchResult.statuses.map { it.toPostCardUiState(usersAccountId) },
                    accountUiStates = searchResult.accounts.map { it.toSearchedAccountUiState() },
                )
            }.collect {
                _uiState.edit { copy(
                    topResource = it
                ) }
            }
        }
    }

    override fun onRetryClicked() {
        onSearchClicked()
    }

    override fun onTabClicked(tab: SearchTab) {
        _uiState.edit { copy(
            selectedTab = tab
        ) }
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
    }

    override fun onAccountClicked(accountId: String) {
        navigateTo(NavigationDestination.Account(accountId = accountId))
    }
}