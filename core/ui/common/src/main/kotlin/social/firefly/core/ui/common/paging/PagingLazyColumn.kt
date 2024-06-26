package social.firefly.core.ui.common.paging

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import social.firefly.core.designsystem.theme.FfTheme
import social.firefly.core.ui.common.R
import social.firefly.core.ui.common.animation.DelayedVisibility
import social.firefly.core.ui.common.error.GenericError
import social.firefly.core.ui.common.loading.FfCircularProgressIndicator

@Composable
fun <A : Any> PagingLazyColumn(
    lazyPagingItems: LazyPagingItems<A>,
    modifier: Modifier = Modifier,
    noResultText: String = stringResource(id = R.string.theres_nothing_here),
    showLoadingSpinnerOnRefresh: Boolean = true,
    listState: LazyListState = rememberLazyListState(),
    emptyListState: LazyListState = rememberLazyListState(),
    headerContent: LazyListScope.() -> Unit = {},
    content: LazyListScope.() -> Unit,
) {
    PagingLazyColumn(
        lazyPagingItems = lazyPagingItems,
        modifier = modifier,
        emptyListContent = {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentWidth(Alignment.CenterHorizontally)
                    .padding(16.dp),
                text = noResultText,
            )
        },
        showLoadingSpinnerOnRefresh = showLoadingSpinnerOnRefresh,
        listState = listState,
        emptyListState = emptyListState,
        headerContent = headerContent,
        content = content
    )
}

@Composable
fun <A : Any> PagingLazyColumn(
    lazyPagingItems: LazyPagingItems<A>,
    modifier: Modifier = Modifier,
    emptyListContent: @Composable () -> Unit,
    showLoadingSpinnerOnRefresh: Boolean = true,
    listState: LazyListState = rememberLazyListState(),
    emptyListState: LazyListState = rememberLazyListState(),
    headerContent: LazyListScope.() -> Unit = {},
    content: LazyListScope.() -> Unit,
) {
    var isRetryingAfterError by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .fillMaxSize(),
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            state = if (lazyPagingItems.itemCount == 0) {
                emptyListState
            } else {
                listState
            },
        ) {
            headerContent()
            when (lazyPagingItems.loadState.refresh) {
                is LoadState.Error -> {} // handle the error outside the lazy column
                is LoadState.Loading -> {
                    if (!isRetryingAfterError) {
                        content()
                    }
                    if (showLoadingSpinnerOnRefresh) {
                        item {
                            DelayedVisibility {
                                FfCircularProgressIndicator(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .wrapContentWidth(Alignment.CenterHorizontally)
                                        .padding(16.dp),
                                )
                            }
                        }
                    }
                }

                is LoadState.NotLoading -> {
                    item(key = lazyPagingItems.itemCount) {
                        DelayedVisibility(
                            key = lazyPagingItems.itemCount,
                            visible = lazyPagingItems.itemCount == 0
                        ) {
                            emptyListContent()
                        }
                    }
                    content()
                }
            }

            when (lazyPagingItems.loadState.append) {
                is LoadState.Loading -> {
                    item {
                        FfCircularProgressIndicator(
                            modifier = Modifier
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

                is LoadState.NotLoading -> {}
            }
        }

        if (lazyPagingItems.loadState.refresh is LoadState.Error) {
            GenericError(
                modifier = Modifier
                    .fillMaxSize()
                    .background(FfTheme.colors.layer1),
                onRetryClicked = {
                    isRetryingAfterError = true
                    lazyPagingItems.refresh()
                },
            )
        } else if (lazyPagingItems.loadState.refresh is LoadState.NotLoading) {
            isRetryingAfterError = false
        }
    }
}
