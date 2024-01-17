package org.mozilla.social.core.ui.common.pullrefresh

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import org.mozilla.social.core.designsystem.theme.MoSoTheme
import org.mozilla.social.core.ui.common.R
import org.mozilla.social.core.ui.common.error.GenericError

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
    content: LazyListScope.() -> Unit,
) {
    Box(
        modifier = Modifier
            .pullRefresh(pullRefreshState)
            .fillMaxSize(),
    ) {
        LazyColumn(
            modifier = modifier,
            state = if (lazyPagingItems.itemCount == 0) {
                emptyListState
            } else {
                listState
            },
        ) {
            when (lazyPagingItems.loadState.refresh) {
                is LoadState.Error -> {} // handle the error outside the lazy column
                else -> {
                    content()
                }
            }

            when (lazyPagingItems.loadState.append) {
                is LoadState.Loading -> {
                    item {
                        CircularProgressIndicator(
                            modifier =
                            Modifier
                                .fillMaxWidth()
                                .wrapContentWidth(Alignment.CenterHorizontally)
                                .padding(16.dp),
                        )
                    }
                }

                is LoadState.Error -> {
                    item {
                        GenericError(
                            onRetryClicked = { lazyPagingItems.retry() },
                        )
                    }
                }

                is LoadState.NotLoading -> {
                    item {
                        if (lazyPagingItems.itemSnapshotList.isEmpty()) { // Show empty state
                            Text(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .wrapContentWidth(Alignment.CenterHorizontally)
                                    .padding(16.dp),
                                text = stringResource(
                                    id =
                                    R.string.theres_nothing_here
                                ),
                            )
                        } else { // It's the end of the list so add some space
                            Spacer(modifier = Modifier.height(50.dp))
                        }
                    }
                }
            }
        }

        if (lazyPagingItems.loadState.refresh is LoadState.Error) {
            GenericError(
                modifier =
                Modifier
                    .fillMaxSize()
                    .background(MoSoTheme.colors.layer1),
                onRetryClicked = { lazyPagingItems.refresh() },
            )
        }

        pullRefreshIndicator(
            lazyPagingItems.loadState.refresh == LoadState.Loading,
            pullRefreshState,
        )
    }
}
