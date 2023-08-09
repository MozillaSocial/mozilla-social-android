package org.mozilla.social.core.ui.postcard

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import kotlinx.coroutines.flow.Flow

@Composable
fun PostCardList(
    feed: Flow<PagingData<PostCardUiState>>,
    postCardInteractions: PostCardInteractions,
) {
    val listState = rememberLazyListState()
    val lazyingPagingItems = feed.collectAsLazyPagingItems()

    LazyColumn(
        Modifier
            .fillMaxSize()
            .padding(4.dp),
        state = listState,
    ) {
        items(
            count = lazyingPagingItems.itemCount,
            key = lazyingPagingItems.itemKey { it.statusId }
        ) { index ->
            lazyingPagingItems[index]?.let { item ->
                Text(text = "$index")
                PostCard(post = item, postCardInteractions)
                if (index < lazyingPagingItems.itemCount) {
                    Divider()
                }
            }
        }
    }
}