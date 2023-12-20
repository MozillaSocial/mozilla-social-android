package org.mozilla.social.search

import org.mozilla.social.common.Resource
import org.mozilla.social.core.ui.common.account.quickview.AccountQuickViewUiState
import org.mozilla.social.core.ui.postcard.PostCardUiState

data class SearchUiState(
    val query: String = "",
    val selectedTab: SearchTab = SearchTab.TOP,
    val topResource: Resource<SearchResultUiState> = Resource.Loading()
)

data class SearchResultUiState(
    val postCardUiStates: List<PostCardUiState>,
    val accountUiStates: List<AccountQuickViewUiState>,
)
