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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import org.koin.androidx.compose.koinViewModel
import social.firefly.core.designsystem.theme.FfSpacing
import social.firefly.core.designsystem.utils.NoRipple
import social.firefly.core.ui.common.FfSurface
import social.firefly.core.ui.common.appbar.FfTopBar
import social.firefly.core.ui.common.hashtag.HashtagInteractions
import social.firefly.core.ui.common.hashtag.hashTagListItems
import social.firefly.core.ui.common.hashtag.quickview.HashTagQuickViewUiState
import social.firefly.core.ui.common.search.FfSearchBar
import social.firefly.core.ui.common.text.LargeTextTitle
import social.firefly.core.ui.common.utils.PreviewTheme
import social.firefly.core.ui.common.utils.shareUrl

@Composable
internal fun DiscoverScreen(viewModel: DiscoverViewModel = koinViewModel()) {
    val hashtagsFeed = viewModel.trendingHashtags
    DiscoverScreen(
        hashTagsFeed = hashtagsFeed,
        discoverInteractions = viewModel,
    )

    LaunchedEffect(Unit) {
        viewModel.onScreenViewed()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DiscoverScreen(
    hashTagsFeed: Flow<PagingData<HashTagQuickViewUiState>>,
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
                hashTagsFeed = hashTagsFeed,
                hashtagInteractions = discoverInteractions,
            )
        }
    }
}

@Composable
private fun MainContent(
    hashTagsFeed: Flow<PagingData<HashTagQuickViewUiState>>,
    hashtagInteractions: HashtagInteractions,
) {
    val feed = hashTagsFeed.collectAsLazyPagingItems()
    LazyColumn {
        item {
            LargeTextTitle(
                modifier =
                Modifier
                    .padding(start = 16.dp, top = 8.dp),
                text = stringResource(id = R.string.discover_title),
            )
        }
        hashTagListItems(
            hashTagsFeed = feed,
            hashtagInteractions = hashtagInteractions,
        )
    }
}


@Preview
@Composable
private fun DiscoverScreenPreview() {
    PreviewTheme {
        DiscoverScreen(
            hashTagsFeed = flowOf(),
            discoverInteractions = DiscoverInteractionsNoOp,
        )
    }
}
