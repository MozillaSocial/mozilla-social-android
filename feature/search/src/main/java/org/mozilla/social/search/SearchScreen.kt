package org.mozilla.social.search

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel
import org.mozilla.social.core.designsystem.icon.MoSoIcons
import org.mozilla.social.core.designsystem.theme.MoSoSpacing
import org.mozilla.social.core.designsystem.theme.MoSoTheme
import org.mozilla.social.core.ui.common.MoSoSearchBar
import org.mozilla.social.core.ui.common.MoSoSurface
import org.mozilla.social.core.ui.common.MoSoTab
import org.mozilla.social.core.ui.common.MoSoTabRow
import org.mozilla.social.core.ui.common.appbar.MoSoCloseableTopAppBar
import org.mozilla.social.core.ui.common.button.MoSoButton

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

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
private fun SearchScreen(
    uiState: SearchUiState,
    searchInteractions: SearchInteractions,
) {
    MoSoSurface {
        Column(Modifier.systemBarsPadding()) {
            val searchFocusRequester = remember { FocusRequester() }
            val keyboardController = LocalSoftwareKeyboardController.current
            val focusManager = LocalFocusManager.current

            MoSoSearchBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(searchFocusRequester)
                    .padding(horizontal = MoSoSpacing.md),
                query = uiState.query,
                onQueryChange = { searchInteractions.onQueryTextChanged(it) },
                onSearch = {
                    keyboardController?.hide()
                    focusManager.clearFocus()
                },
                leadingIcon = {
                    Icon(
                        modifier = Modifier.size(16.dp),
                        painter = MoSoIcons.magnifyingGlass(),
                        contentDescription = "",
                        tint = MoSoTheme.colors.iconPrimary,
                    )
                }
            )

            LaunchedEffect(Unit) {
                searchFocusRequester.requestFocus()
            }

            Spacer(modifier = Modifier.height(8.dp))

            val context = LocalContext.current

            MoSoTabRow(selectedTabIndex = uiState.selectedTab.ordinal) {
                SearchTab.entries.forEach { tabType ->
                    MoSoTab(
                        modifier =
                        Modifier
                            .height(40.dp),
                        selected = uiState.selectedTab == tabType,
                        onClick = { searchInteractions.onTabClicked(tabType) },
                        content = {
                            Text(
                                text = tabType.tabTitle.build(context),
                                style = MoSoTheme.typography.labelMedium,
                            )
                        },
                    )
                }
            }
        }
    }
}