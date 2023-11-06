package org.mozilla.social.core.ui.postcard

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import org.mozilla.social.core.ui.common.MoSoErrorToast
import org.mozilla.social.core.ui.common.error.GenericError
import org.mozilla.social.core.ui.common.loading.MaxSizeLoading
import org.mozilla.social.core.ui.common.pullrefresh.PullRefreshIndicator
import org.mozilla.social.core.ui.common.pullrefresh.pullRefresh
import org.mozilla.social.core.ui.common.pullrefresh.rememberPullRefreshState

/**
 * Shows a list of post cards and various loading and error states.
 *
 * @param errorToastMessage a flow of toast messages to show when an error happens
 * @param refreshSignalFlow a flow that causes the list to refresh when it's emitted to
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
    refreshSignalFlow: Flow<Any>? = null,
    postCardInteractions: PostCardInteractions,
    pullToRefreshEnabled: Boolean = false,
    isFullScreenLoading: Boolean = false,
    scrollState: LazyListState = rememberLazyListState(),
    headerContent: @Composable () -> Unit = {},
) {
    val lazyingPagingItems: LazyPagingItems<PostCardUiState> = feed.collectAsLazyPagingItems()

    LaunchedEffect(Unit) {
        refreshSignalFlow?.collect {
            lazyingPagingItems.refresh()
        }
    }

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

        PostCardLazyColumn(
            lazyingPagingItems = lazyingPagingItems,
            postCardInteractions = postCardInteractions,
            pullToRefreshEnabled = pullToRefreshEnabled,
            isFullScreenLoading = isFullScreenLoading,
            scrollState = scrollState,
            emptyListState = rememberLazyListState(),
            headerContent = headerContent,
        )

        if (lazyingPagingItems.loadState.refresh is LoadState.Error && isFullScreenLoading) {
            GenericError(
                modifier = Modifier
                    .fillMaxSize(),
                onRetryClicked = { lazyingPagingItems.refresh() }
            )
        }

        if (lazyingPagingItems.loadState.refresh is LoadState.Loading && !pullToRefreshEnabled && isFullScreenLoading) {
            MaxSizeLoading()
        }

        if (pullToRefreshEnabled) {
            PullRefreshIndicator(
                modifier = Modifier.align(Alignment.TopCenter),
                refreshing = lazyingPagingItems.loadState.refresh == LoadState.Loading,
                state = pullRefreshState,
            )
        }
    }

    MoSoErrorToast(toastMessage = errorToastMessage)
}

@Composable
private fun PostCardLazyColumn(
    lazyingPagingItems: LazyPagingItems<PostCardUiState>,
    postCardInteractions: PostCardInteractions,
    pullToRefreshEnabled: Boolean,
    isFullScreenLoading: Boolean,
    scrollState: LazyListState,
    emptyListState: LazyListState,
    headerContent: @Composable () -> Unit,
) {

    // When navigating back to a list, the lazyPagingItems seem to have a list size of 0
    // for a split second before going back to where it was.  This causes the list scroll state
    // to reset at 0, losing the scroll position.  The emptyListState variable fixes this by
    // only being in use when the item count is 0.
    // There is an issue in google issue tracker
    // https://issuetracker.google.com/issues/179397301
    // There is also a fixed issue that is related to this
    // https://issuetracker.google.com/issues/177245496

    LazyColumn(
        Modifier
            .fillMaxSize(),
        state = if (lazyingPagingItems.itemCount == 0) {
            emptyListState
        } else {
            scrollState
        },
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
            PostCard(
                post = item,
                postCardInteractions = postCardInteractions,
            )
            if (index < lazyingPagingItems.itemCount) {
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
    errorToastMessage: SharedFlow<StringFactory>,
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
            key = { items[it].statusId }
        ) { index ->
            val item = items[index]
            PostCard(
                post = item,
                postCardInteractions = postCardInteractions,
                threadId = threadId
            )
            if (index < items.count()) {
                MoSoDivider()
            }
        }
    }

    MoSoErrorToast(toastMessage = errorToastMessage)
}
