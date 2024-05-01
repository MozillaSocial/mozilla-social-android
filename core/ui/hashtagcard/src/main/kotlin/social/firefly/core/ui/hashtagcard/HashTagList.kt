package social.firefly.core.ui.hashtagcard

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.ui.Modifier
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemKey
import social.firefly.core.designsystem.theme.FfSpacing
import social.firefly.core.ui.hashtagcard.quickview.HashTagQuickView
import social.firefly.core.ui.hashtagcard.quickview.HashTagQuickViewUiState

fun LazyListScope.hashTagListItems(
    hashTagsFeed: LazyPagingItems<HashTagQuickViewUiState>,
    hashtagInteractions: HashTagInteractions,
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
