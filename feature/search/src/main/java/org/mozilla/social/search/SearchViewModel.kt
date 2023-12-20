package org.mozilla.social.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.parameter.parametersOf
import org.koin.java.KoinJavaComponent
import org.mozilla.social.common.utils.edit
import org.mozilla.social.core.analytics.AnalyticsIdentifiers
import org.mozilla.social.core.model.SearchType
import org.mozilla.social.core.repository.mastodon.SearchRepository
import org.mozilla.social.core.ui.postcard.PostCardDelegate
import org.mozilla.social.core.ui.postcard.toPostCardUiState
import org.mozilla.social.core.usecase.mastodon.account.GetLoggedInUserAccountId
import org.mozilla.social.core.usecase.mastodon.search.SearchAll
import timber.log.Timber

class SearchViewModel(
    private val searchAll: SearchAll,
    getLoggedInUserAccountId: GetLoggedInUserAccountId,
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

    override fun onQueryTextChanged(text: String) {
        _uiState.edit { copy(
            query = text,
        ) }
    }

    override fun onSearchClicked() {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            searchAll(
                uiState.value.query,
                viewModelScope,
            ) { searchResult ->
                SearchResultUiState(
                    searchResult.statuses.map { it.toPostCardUiState(usersAccountId) }
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
}