package social.firefly.feature.bookmarks

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import kotlinx.coroutines.flow.Flow
import org.koin.androidx.compose.koinViewModel
import social.firefly.core.ui.common.FfSurface
import social.firefly.core.ui.common.UiConstants
import social.firefly.core.ui.common.appbar.FfCloseableTopAppBar
import social.firefly.core.ui.common.pullrefresh.PullRefreshLazyColumn
import social.firefly.core.ui.postcard.PostCardInteractions
import social.firefly.core.ui.postcard.PostCardUiState
import social.firefly.core.ui.postcard.postListContent

@Composable
internal fun BookmarksScreen(
    viewModel: BookmarksViewModel = koinViewModel()
) {
    BookmarksScreen(
        feed = viewModel.feed,
        postCardInteractions = viewModel.postCardDelegate
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BookmarksScreen(
    feed: Flow<PagingData<PostCardUiState>>,
    postCardInteractions: PostCardInteractions,
) {
    FfSurface {
        Column(
            modifier = Modifier.systemBarsPadding(),
        ) {
            FfCloseableTopAppBar(title = stringResource(id = R.string.bookmarks_screen_title))

            val feedListState = feed.collectAsLazyPagingItems()

            PullRefreshLazyColumn(
                modifier = Modifier
                    .widthIn(max = UiConstants.MAX_WIDTH)
                    .align(Alignment.CenterHorizontally),
                lazyPagingItems = feedListState,
            ) {
                postListContent(
                    lazyPagingItems = feedListState,
                    postCardInteractions = postCardInteractions,
                )
            }
        }
    }
}