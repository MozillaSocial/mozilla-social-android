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
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
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
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import kotlinx.coroutines.flow.Flow
import org.koin.androidx.compose.koinViewModel
import social.firefly.core.designsystem.theme.FfSpacing
import social.firefly.core.designsystem.utils.NoRipple
import social.firefly.core.ui.common.FfSurface
import social.firefly.core.ui.common.appbar.FfTopBar
import social.firefly.core.ui.hashtagcard.HashTagInteractions
import social.firefly.core.ui.hashtagcard.hashTagListItems
import social.firefly.core.ui.hashtagcard.quickview.HashTagQuickViewUiState
import social.firefly.core.ui.common.pullrefresh.PullRefreshLazyColumn
import social.firefly.core.ui.common.search.FfSearchBar
import social.firefly.core.ui.common.tabs.FfTab
import social.firefly.core.ui.common.tabs.FfTabRow
import social.firefly.core.ui.common.text.MediumTextLabel
import social.firefly.core.ui.common.utils.PreviewTheme
import social.firefly.core.ui.postcard.PostCardInteractions
import social.firefly.core.ui.postcard.PostCardUiState
import social.firefly.core.ui.postcard.postListContent

@Composable
internal fun DiscoverScreen(viewModel: DiscoverViewModel = koinViewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    DiscoverScreen(
        uiState = uiState,
        discoverInteractions = viewModel,
        postCardInteractions = viewModel.postCardDelegate,
        hashtagInteractions = viewModel.hashTagCardDelegate,
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
    postCardInteractions: PostCardInteractions,
    hashtagInteractions: HashTagInteractions,
) {
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
                    val searchField = stringResource(id = R.string.search_field)
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
                postCardInteractions = postCardInteractions,
                hashtagInteractions = hashtagInteractions,
            )
        }
    }
}

@Composable
private fun MainContent(
    uiState: DiscoverUiState,
    discoverInteractions: DiscoverInteractions,
    postCardInteractions: PostCardInteractions,
    hashtagInteractions: HashTagInteractions,
) {
    Box {
        Column {
            Tabs(
                uiState = uiState,
                discoverInteractions = discoverInteractions,
            )

            val hashTagLazyPagingItems = (uiState.tabs.find { it is DiscoverTab.Hashtags } as DiscoverTab.Hashtags)
                .pagingDataFlow
                .collectAsLazyPagingItems()
            val hashTagListState = rememberLazyListState()

            val postsLazyPagingItems = (uiState.tabs.find { it is DiscoverTab.Posts } as DiscoverTab.Posts)
                .pagingDataFlow
                .collectAsLazyPagingItems()
            val postListState = rememberLazyListState()

            when (uiState.selectedTab) {
                is DiscoverTab.Hashtags -> {
                    Hashtags(
                        feed = hashTagLazyPagingItems,
                        listState = hashTagListState,
                        hashtagInteractions = hashtagInteractions,
                    )
                }

                is DiscoverTab.Posts -> {
                    Posts(
                        feed = postsLazyPagingItems,
                        listState = postListState,
                        postCardInteractions = postCardInteractions,
                    )
                }
            }
        }
    }
}

@Composable
private fun Tabs(
    uiState: DiscoverUiState,
    discoverInteractions: DiscoverInteractions,
) {
    val context = LocalContext.current

    FfTabRow(
        selectedTabIndex = uiState.tabs.indexOf(uiState.selectedTab)
    ) {
        uiState.tabs.forEach { tab ->
            FfTab(
                modifier = Modifier
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

@Composable
private fun Hashtags(
    feed: LazyPagingItems<HashTagQuickViewUiState>,
    listState: LazyListState,
    hashtagInteractions: HashTagInteractions,
) {
    PullRefreshLazyColumn(
        lazyPagingItems = feed,
        listState = listState,
    ) {
        hashTagListItems(
            hashTagsFeed = feed,
            hashtagInteractions = hashtagInteractions,
        )
    }
}

@Composable
private fun Posts(
    feed: LazyPagingItems<PostCardUiState>,
    listState: LazyListState,
    postCardInteractions: PostCardInteractions,
) {
    PullRefreshLazyColumn(
        lazyPagingItems = feed,
        listState = listState,
    ) {
        postListContent(
            lazyPagingItems = feed,
            postCardInteractions = postCardInteractions,
        )
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
