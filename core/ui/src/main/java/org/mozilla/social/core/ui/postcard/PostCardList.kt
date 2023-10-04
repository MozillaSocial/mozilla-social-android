package org.mozilla.social.core.ui.postcard

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow
import org.mozilla.social.common.utils.StringFactory
import org.mozilla.social.core.designsystem.component.MoSoCircularProgressIndicator
import org.mozilla.social.core.designsystem.component.MoSoDivider
import org.mozilla.social.core.designsystem.component.MoSoToast
import org.mozilla.social.core.ui.error.GenericError
import org.mozilla.social.core.ui.pullrefresh.PullRefreshIndicator
import org.mozilla.social.core.ui.pullrefresh.pullRefresh
import org.mozilla.social.core.ui.pullrefresh.rememberPullRefreshState

/**
 * Shows a list of post cards and various loading and error states.
 *
 * @param errorToastMessage a flow of toast messages to show when an error happens
 * @param pullToRefreshEnabled if true, the user will be able to pull to refresh, and
 * the pull to refresh loading indicator will be used when doing an initial load or a refresh.
 * @param isFullScreenLoading if false, loading and error states will appear below the header
 * If true, loading and error states will be centered and full screen.
 * Note that if [pullToRefreshEnabled] is true, the pull to refresh loading indicator will be used instead.
 * @param headerContent content that will show above the list
 */
@Composable
fun PostCardList(
    feed: Flow<PagingData<PostCardUiState>>,
    errorToastMessage: SharedFlow<StringFactory>,
    postCardInteractions: PostCardInteractions,
    pullToRefreshEnabled: Boolean = false,
    isFullScreenLoading: Boolean = false,
    headerContent: @Composable () -> Unit = {},
) {

    val lazyingPagingItems: LazyPagingItems<PostCardUiState> = feed.collectAsLazyPagingItems()

    val pullRefreshState = rememberPullRefreshState(
        refreshing = lazyingPagingItems.loadState.refresh == LoadState.Loading,
        onRefresh = { lazyingPagingItems.refresh() }
    )

    Box(
        modifier = Modifier
            .pullRefresh(
                pullRefreshState,
                enabled = pullToRefreshEnabled,
            ),
    ) {

        LazyColumn(
            Modifier
                .fillMaxSize(),
        ) {

            item { headerContent() }

            when (lazyingPagingItems.loadState.refresh) {
                is LoadState.Error -> {
                    if (!isFullScreenLoading) {
                        item {
                            GenericError(
                                modifier = Modifier
                                    .fillMaxSize(),
                                onRetryClicked = { lazyingPagingItems.refresh() }
                            )
                        }
                    }
                }
                is LoadState.Loading -> {
                    if (!isFullScreenLoading && !pullToRefreshEnabled) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .wrapContentWidth(Alignment.CenterHorizontally)
                                    .padding(top = 40.dp)
                            ) {
                                MoSoCircularProgressIndicator()
                            }
                        }
                    }
                    if (pullToRefreshEnabled) {
                        listContent(lazyingPagingItems, postCardInteractions)
                    }
                }
                is LoadState.NotLoading -> {
                    listContent(lazyingPagingItems, postCardInteractions)
                }
            }

            when (lazyingPagingItems.loadState.append) {
                is LoadState.Loading -> {
                    item {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentWidth(Alignment.CenterHorizontally)
                                .padding(16.dp)
                        )
                    }
                }

                is LoadState.Error -> {
                    item {
                        GenericError(
                            onRetryClicked = { lazyingPagingItems.retry() }
                        )
                    }
                }

                is LoadState.NotLoading -> {}
            }
        }

        if (lazyingPagingItems.loadState.refresh is LoadState.Error && isFullScreenLoading) {
            GenericError(
                modifier = Modifier
                    .fillMaxSize(),
                onRetryClicked = { lazyingPagingItems.refresh() }
            )
        }

        if (lazyingPagingItems.loadState.refresh is LoadState.Loading && !pullToRefreshEnabled && isFullScreenLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentSize(Alignment.Center)
            ) {
                MoSoCircularProgressIndicator()
            }
        }

        if (pullToRefreshEnabled) {
            PullRefreshIndicator(
                modifier = Modifier.align(Alignment.TopCenter),
                refreshing = lazyingPagingItems.loadState.refresh == LoadState.Loading,
                state = pullRefreshState,
            )
        }
    }

    MoSoToast(toastMessage = errorToastMessage)
}

private fun LazyListScope.listContent(
    lazyingPagingItems: LazyPagingItems<PostCardUiState>,
    postCardInteractions: PostCardInteractions,
) {
    items(
        count = lazyingPagingItems.itemCount,
        key = lazyingPagingItems.itemKey { it.statusId }
    ) { index ->
        lazyingPagingItems[index]?.let { item ->
            PostCard(post = item, postCardInteractions)
            if (index < lazyingPagingItems.itemCount) {
                MoSoDivider()
            }
        }
    }
}

@Composable
fun PostCardList(
    items: List<PostCardUiState>,
    errorToastMessage: SharedFlow<StringFactory>,
    postCardInteractions: PostCardInteractions,
) {

    LazyColumn(
        Modifier
            .fillMaxSize()
            .padding(horizontal = 4.dp),
    ) {
        items(
            count = items.count(),
            key = { items[it].statusId }
        ) { index ->
            val item = items[index]
            PostCard(post = item, postCardInteractions)
            if (index < items.count()) {
                MoSoDivider()
            }
        }
    }

    MoSoToast(toastMessage = errorToastMessage)
}
