package org.mozilla.social.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.mozilla.social.common.utils.edit
import org.mozilla.social.core.model.SearchType
import org.mozilla.social.core.repository.mastodon.SearchRepository
import timber.log.Timber

class SearchViewModel(
    private val searchRepository: SearchRepository,
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
            try {
                searchRepository.search(
                    query = uiState.value.query,
                    type = SearchType.Accounts,
                )
            } catch (e: Exception) {
                Timber.e(e)
            }
        }
    }

    override fun onTabClicked(tab: SearchTab) {
        _uiState.edit { copy(
            selectedTab = tab
        ) }
    }
}