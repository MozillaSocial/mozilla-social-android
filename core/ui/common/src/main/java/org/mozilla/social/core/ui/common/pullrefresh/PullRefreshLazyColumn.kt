package org.mozilla.social.core.ui.common.pullrefresh

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import org.mozilla.social.core.ui.common.paging.PagingLazyColumn

@Composable
fun <A : Any> PullRefreshLazyColumn(
    lazyPagingItems: LazyPagingItems<A>,
    modifier: Modifier = Modifier,
    pullRefreshState: PullRefreshState = rememberPullRefreshState(
        refreshing = lazyPagingItems.loadState.refresh == LoadState.Loading,
        onRefresh = { lazyPagingItems.refresh() },
    ),
    pullRefreshIndicator: @Composable BoxScope.(refreshing: Boolean, pullRefreshState: PullRefreshState) -> Unit =
        { refreshing, state ->
            PullRefreshIndicator(
                modifier = Modifier.align(Alignment.TopCenter),
                refreshing = refreshing,
                state = state
            )

        },
    listState: LazyListState = rememberLazyListState(),
    emptyListState: LazyListState = rememberLazyListState(),
    headerContent: LazyListScope.() -> Unit = {},
    content: LazyListScope.() -> Unit,
) {
    Box(
        modifier = modifier
            .pullRefresh(pullRefreshState)
            .fillMaxSize(),
    ) {
        PagingLazyColumn(
            lazyPagingItems = lazyPagingItems,
            listState = listState,
            emptyListState = emptyListState,
            headerContent = headerContent,
            content = content,
        )

        pullRefreshIndicator(
            lazyPagingItems.loadState.refresh == LoadState.Loading,
            pullRefreshState,
        )
    }
}
