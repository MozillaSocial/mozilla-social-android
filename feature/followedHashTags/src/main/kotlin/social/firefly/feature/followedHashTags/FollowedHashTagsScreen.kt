package social.firefly.feature.followedHashTags

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import kotlinx.coroutines.flow.Flow
import org.koin.androidx.compose.koinViewModel
import social.firefly.core.ui.common.FfSurface
import social.firefly.core.ui.common.UiConstants
import social.firefly.core.ui.common.appbar.FfCloseableTopAppBar
import social.firefly.core.ui.common.pullrefresh.PullRefreshLazyColumn
import social.firefly.core.ui.hashtagcard.HashTagInteractions
import social.firefly.core.ui.hashtagcard.hashTagListItems
import social.firefly.core.ui.hashtagcard.quickview.HashTagQuickViewUiState

@Composable
internal fun FollowedHashTagsScreen(
    viewModel: FollowedHashTagsViewModel = koinViewModel()
) {
    FollowedHashTagsScreen(
        feed = viewModel.feed,
        hashTagInteractions = viewModel.hashTagCardDelegate,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FollowedHashTagsScreen(
    feed: Flow<PagingData<HashTagQuickViewUiState>>,
    hashTagInteractions: HashTagInteractions,
) {
    FfSurface {
        Column(
            modifier = Modifier.systemBarsPadding()
        ) {
            FfCloseableTopAppBar(title = stringResource(id = R.string.followed_hash_tags_title))

            val feedListState = feed.collectAsLazyPagingItems()

            PullRefreshLazyColumn(
                modifier = Modifier
                    .widthIn(max = UiConstants.MAX_WIDTH)
                    .align(Alignment.CenterHorizontally),
                lazyPagingItems = feedListState,
            ) {
                hashTagListItems(
                    hashTagsFeed = feedListState,
                    hashtagInteractions = hashTagInteractions,
                )
            }
        }
    }
}