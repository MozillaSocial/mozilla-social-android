package org.mozilla.social.search

interface SearchInteractions {
    fun onQueryTextChanged(text: String) = Unit
    fun onSearchClicked() = Unit
}