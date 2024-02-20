package social.firefly.core.ui.postcard

import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.Composable
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemKey
import social.firefly.core.ui.common.divider.FfDivider

fun LazyListScope.postListContent(
    lazyPagingItems: LazyPagingItems<PostCardUiState>,
    postCardInteractions: PostCardInteractions,
) {
    items(
        count = lazyPagingItems.itemCount,
        key = lazyPagingItems.itemKey { it.statusId },
    ) { index ->
        lazyPagingItems[index]?.let { item ->
            PostCardListItem(
                uiState = item,
                postCardInteractions = postCardInteractions,
                index = index,
                itemCount = lazyPagingItems.itemCount,
            )
        }
    }
}

@Composable
fun PostCardListItem(
    uiState: PostCardUiState,
    postCardInteractions: PostCardInteractions,
    index: Int,
    itemCount: Int,
    threadId: String? = null,
) {
    PostCard(
        post = uiState,
        postCardInteractions = postCardInteractions,
        threadId = threadId,
    )
    if (index < itemCount) {
        FfDivider()
    }
}
