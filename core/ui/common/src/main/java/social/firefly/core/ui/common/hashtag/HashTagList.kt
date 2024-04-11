package social.firefly.core.ui.common.hashtag

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemKey
import social.firefly.core.designsystem.theme.FfSpacing
import social.firefly.core.ui.common.R
import social.firefly.core.ui.common.following.FollowStatus
import social.firefly.core.ui.common.hashtag.quickview.HashTagQuickView
import social.firefly.core.ui.common.hashtag.quickview.HashTagQuickViewUiState
import social.firefly.core.ui.common.paging.PagingLazyColumn

@Composable
fun HashTagList(
    hashTagsFeed: LazyPagingItems<HashTagQuickViewUiState>?,
    noResultText: String = stringResource(id = R.string.theres_nothing_here),
    scrollState: LazyListState,
    hashtagInteractions: HashtagInteractions,
) {
    hashTagsFeed?.let { lazyPagingItems ->
        PagingLazyColumn(
            lazyPagingItems = lazyPagingItems,
            noResultText = noResultText,
            listState = scrollState,
        ) {
            hashTagListItems(
                hashTagsFeed = lazyPagingItems,
                hashtagInteractions = hashtagInteractions,
            )
        }
    }
}

fun LazyListScope.hashTagListItems(
    hashTagsFeed: LazyPagingItems<HashTagQuickViewUiState>,
    hashtagInteractions: HashtagInteractions,
) {
    items(
        count = hashTagsFeed.itemCount,
        key = hashTagsFeed.itemKey { it.name },
    ) { index ->
        hashTagsFeed[index]?.let { uiState ->
            HashTagQuickView(
                modifier = Modifier
                    .padding(FfSpacing.md)
                    .clickable { hashtagInteractions.onHashtagClick(uiState.name) },
                uiState = uiState,
                onButtonClicked = {
                    hashtagInteractions.onHashTagFollowClicked(
                        name = uiState.name,
                        followStatus = uiState.followStatus
                    )
                }
            )
        }
    }
}

interface HashtagInteractions {
    fun onHashTagFollowClicked(name: String, followStatus: FollowStatus)
    fun onHashtagClick(name: String)
}