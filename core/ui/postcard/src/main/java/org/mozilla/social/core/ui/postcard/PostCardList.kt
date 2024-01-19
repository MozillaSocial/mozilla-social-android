package org.mozilla.social.core.ui.postcard

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemKey
import org.mozilla.social.core.ui.common.divider.MoSoDivider

fun LazyListScope.postListContent(
    lazyPagingItems: LazyPagingItems<PostCardUiState>,
    postCardInteractions: PostCardInteractions,
) {
    items(
        count = lazyPagingItems.itemCount,
        key = lazyPagingItems.itemKey { it.statusId },
    ) { index ->
        lazyPagingItems[index]?.let { item ->
            PostCard(
                post = item,
                postCardInteractions = postCardInteractions,
            )
            if (index < lazyPagingItems.itemCount) {
                MoSoDivider()
            }
        }
    }
}

/**
 * @param threadId if viewing a thread, pass the threadId in to prevent
 * the user from being able to click on the same status as the thread's root status
 */
@Composable
fun PostCardList(
    items: List<PostCardUiState>,
    postCardInteractions: PostCardInteractions,
    threadId: String? = null,
) {
    LazyColumn(
        Modifier
            .fillMaxSize()
            .padding(horizontal = 4.dp),
    ) {
        items(
            count = items.count(),
            key = { items[it].statusId },
        ) { index ->
            val item = items[index]
            PostCard(
                post = item,
                postCardInteractions = postCardInteractions,
                threadId = threadId,
            )
            if (index < items.count()) {
                MoSoDivider()
            }
        }
    }
}
