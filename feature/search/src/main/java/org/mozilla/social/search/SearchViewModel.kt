package org.mozilla.social.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.mozilla.social.common.utils.edit
import org.mozilla.social.core.model.SearchType
import org.mozilla.social.core.repository.mastodon.SearchRepository
import org.mozilla.social.core.usecase.mastodon.search.SearchAll
import timber.log.Timber

class SearchViewModel(
    private val searchAll: SearchAll,
) : ViewModel(), SearchInteractions {

    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState = _uiState.asStateFlow()

    override fun onQueryTextChanged(text: String) {
        _uiState.edit { copy(
            query = text,
        ) }
    }

    override fun onSearchClicked() {
        viewModelScope.launch {
            searchAll(
                uiState.value.query,
                viewModelScope,
            ).collect {
                println("status count test ${it.data?.statuses?.count()}")
            }
        }
    }

    override fun onTabClicked(tab: SearchTab) {
        _uiState.edit { copy(
            selectedTab = tab
        ) }
    }
}