package org.mozilla.social.core.ui.postcard

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import kotlinx.coroutines.flow.Flow
import org.mozilla.social.core.ui.pullrefresh.PullRefreshIndicator
import org.mozilla.social.core.ui.pullrefresh.pullRefresh
import org.mozilla.social.core.ui.pullrefresh.rememberPullRefreshState
import org.mozilla.social.core.ui.recommendations.MoreInfoDialog
import org.mozilla.social.core.ui.recommendations.RecommendationCarousel
import org.mozilla.social.model.Recommendation

@Composable
fun PostCardList(
    feed: Flow<PagingData<PostCardUiState>>,
    reccs: List<Recommendation>?,
    postCardInteractions: PostCardInteractions,
) {

    val lazyingPagingItems: LazyPagingItems<PostCardUiState> = feed.collectAsLazyPagingItems()

    val openAlertDialog = remember { mutableStateOf(false) }

    if (openAlertDialog.value) {
        MoreInfoDialog(
            onDismissRequest = { openAlertDialog.value = false },
            onConfirmation = { openAlertDialog.value = false },
            dialogTitle = "Why am I seeing this?",
            dialogText = "You're seeing this because it's the same thing we show to everyone.",
        )
    }

    val pullRefreshState = rememberPullRefreshState(
        refreshing = lazyingPagingItems.loadState.refresh == LoadState.Loading,
        onRefresh = { lazyingPagingItems.refresh() }
    )

    Box(
        modifier = Modifier
            .pullRefresh(pullRefreshState),
    ) {

        LazyColumn(
            Modifier
                .fillMaxSize()
                .padding(4.dp),
        ) {

            reccs?.let {
                item {
                    RecommendationCarousel(reccs = it) { openAlertDialog.value = true }
                }
            }

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


            when (lazyingPagingItems.loadState.append) {
                is LoadState.Loading -> {
                    item {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentWidth(Alignment.CenterHorizontally)
                        )
                    }
                }

                is LoadState.Error -> {
                    item {
                        Button(
                            modifier = Modifier
                                .padding(8.dp),
                            onClick = { lazyingPagingItems.retry() }) {
                            Text(text = "Something went wrong")
                        }
                    }
                }

                is LoadState.NotLoading -> {

                }
            }
        }

        PullRefreshIndicator(
            modifier = Modifier.align(Alignment.TopCenter),
            refreshing = lazyingPagingItems.loadState.refresh == LoadState.Loading,
            state = pullRefreshState,
        )
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
