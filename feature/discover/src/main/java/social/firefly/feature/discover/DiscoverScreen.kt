package social.firefly.feature.discover

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import kotlinx.coroutines.flow.Flow
import org.koin.androidx.compose.koinViewModel
import social.firefly.core.designsystem.theme.FfSpacing
import social.firefly.core.designsystem.utils.NoRipple
import social.firefly.core.ui.common.FfSurface
import social.firefly.core.ui.common.appbar.FfTopBar
import social.firefly.core.ui.common.hashtag.HashtagInteractions
import social.firefly.core.ui.common.hashtag.hashTagListItems
import social.firefly.core.ui.common.hashtag.quickview.HashTagQuickViewUiState
import social.firefly.core.ui.common.search.FfSearchBar
import social.firefly.core.ui.common.tabs.FfTab
import social.firefly.core.ui.common.tabs.FfTabRow
import social.firefly.core.ui.common.text.MediumTextLabel
import social.firefly.core.ui.common.utils.PreviewTheme

@Composable
internal fun DiscoverScreen(viewModel: DiscoverViewModel = koinViewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    DiscoverScreen(
        uiState = uiState,
        discoverInteractions = viewModel,
    )

    LaunchedEffect(Unit) {
        viewModel.onScreenViewed()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DiscoverScreen(
    uiState: DiscoverUiState,
    discoverInteractions: DiscoverInteractions,
) {
    val searchField = stringResource(id = R.string.search_field)
    FfSurface(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Max),
            ) {
                // Adding an empty top bar ensure the search bar will align with the
                // search bar on the search screen
                FfTopBar(title = { })
                FfSearchBar(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.CenterEnd)
                        .padding(
                            end = FfSpacing.md,
                            start = FfSpacing.md,
                        ),
                    query = "",
                    onQueryChange = {},
                    onSearch = {},
                    readOnly = true,
                )
                NoRipple {
                    // invisible box that intercepts clicks
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clickable { discoverInteractions.onSearchBarClicked() }
                            .semantics {
                                contentDescription = searchField
                            }
                    )
                }
            }
            MainContent(
                uiState = uiState,
                discoverInteractions = discoverInteractions,
            )
        }
    }
}

@Composable
private fun MainContent(
    uiState: DiscoverUiState,
    discoverInteractions: DiscoverInteractions,
) {
    Box {
        Column {
            Tabs(
                uiState = uiState,
                discoverInteractions = discoverInteractions,
            )

            when (uiState.selectedTab) {
                is DiscoverTab.Hashtags -> {
                    Hashtags(
                        hashtags = uiState.selectedTab.hashtags,
                        hashtagInteractions = discoverInteractions
                    )
                }

                is DiscoverTab.Posts -> TODO()
            }
        }
    }
}

@Composable
private fun Hashtags(
    hashtags: Flow<PagingData<HashTagQuickViewUiState>>,
    hashtagInteractions: HashtagInteractions,
) {
    val feed = hashtags.collectAsLazyPagingItems()
    LazyColumn {
        hashTagListItems(
            hashTagsFeed = feed,
            hashtagInteractions = hashtagInteractions,
        )
    }
}

@Composable
private fun Tabs(
    uiState: DiscoverUiState,
    discoverInteractions: DiscoverInteractions,
) {
    val context = LocalContext.current

    FfTabRow(selectedTabIndex = uiState.selectedTab.index) {
        uiState.tabs.forEach { tab ->
            FfTab(
                modifier =
                Modifier
                    .height(40.dp),
                selected = uiState.selectedTab == tab,
                onClick = { discoverInteractions.onTabClicked(tab) },
                content = {
                    MediumTextLabel(text = tab.tabTitle.build(context))
                },
            )
        }
    }
}

@Preview
@Composable
private fun DiscoverScreenPreview() {
    PreviewTheme {
//        DiscoverScreen(
//            uiState = flowOf(),
//            discoverInteractions = DiscoverInteractionsNoOp,
//        )
    }
}
