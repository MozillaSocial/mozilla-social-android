package org.mozilla.social.core.ui.postcard

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.count

@Composable
fun PostCardList(
    feed: Flow<PagingData<PostCardUiState>>,
    postCardInteractions: PostCardInteractions,
) {
    val lazyingPagingItems = feed.collectAsLazyPagingItems()

    LazyColumn(
        Modifier
            .fillMaxSize()
            .padding(4.dp),
    ) {
        items(
            count = lazyingPagingItems.itemCount,
            key = lazyingPagingItems.itemKey { it.statusId }
        ) { index ->
            lazyingPagingItems[index]?.let { item ->
                PostCard(post = item, postCardInteractions)
                if (index < lazyingPagingItems.itemCount) {
                    Divider()
                }
            }
        }
    }
}

@Composable
fun PostCardList(
    items: List<PostCardUiState>,
    postCardInteractions: PostCardInteractions,
) {

    LazyColumn(
        Modifier
            .fillMaxSize()
            .padding(4.dp),
    ) {
        items(
            count = items.count(),
            key = { items[it].statusId }
        ) { index ->
            val item = items[index]
            PostCard(post = item, postCardInteractions)
            if (index < items.count()) {
                Divider()
            }
        }
    }
}