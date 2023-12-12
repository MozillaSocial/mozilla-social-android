package org.mozilla.social.search

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SearchBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel
import org.mozilla.social.core.designsystem.icon.MoSoIcons
import org.mozilla.social.core.ui.common.MoSoSurface
import org.mozilla.social.core.ui.common.appbar.MoSoCloseableTopAppBar

@Composable
internal fun SearchScreen(
    viewModel: SearchViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    SearchScreen(
        uiState = uiState,
        searchInteractions = viewModel,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SearchScreen(
    uiState: SearchUiState,
    searchInteractions: SearchInteractions,
) {
    MoSoSurface {
        Column(Modifier.systemBarsPadding()) {
            MoSoCloseableTopAppBar(
                actions = {
                    SearchBar(
                        query = uiState.query,
                        onQueryChange = { searchInteractions.onQueryTextChanged(it) },
                        onSearch = {},
                        active = false,
                        onActiveChange = {},
                        leadingIcon = {
                            Icon(painter = MoSoIcons.magnifyingGlass(), contentDescription = "")
                        }
                    ) {
                        
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                }
            )
        }
    }
}