package org.mozilla.social.search

data class SearchUiState(
    val query: String = "",
    val selectedTab: SearchTab = SearchTab.TOP,
)