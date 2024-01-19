package org.mozilla.social.core.ui.common.paging

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
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
import org.mozilla.social.core.ui.common.animation.DelayedVisibility
import org.mozilla.social.core.ui.common.error.GenericError

@Composable
fun <A : Any> PagingLazyColumn(
    lazyPagingItems: LazyPagingItems<A>,
    modifier: Modifier = Modifier,
    noResultText: String = stringResource(id = R.string.theres_nothing_here),
    listState: LazyListState = rememberLazyListState(),
    emptyListState: LazyListState = rememberLazyListState(),
    headerContent: LazyListScope.() -> Unit = {},
    content: LazyListScope.() -> Unit,
) {
    Box(
        modifier = modifier
            .fillMaxSize(),
    ) {
        LazyColumn(
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
                    content()
                    item {
                        DelayedVisibility {
                            CircularProgressIndicator(
                                modifier =
                                Modifier
                                    .fillMaxWidth()
                                    .wrapContentWidth(Alignment.CenterHorizontally)
                                    .padding(16.dp),
                            )
                        }
                    }
                }
                is LoadState.NotLoading -> {
                    item {
                        DelayedVisibility(
                            key = lazyPagingItems.itemCount,
                            visible = lazyPagingItems.itemCount == 0
                        ) {
                            Text(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .wrapContentWidth(Alignment.CenterHorizontally)
                                    .padding(16.dp),
                                text = noResultText,
                            )
                        }
                    }
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
                        if (lazyPagingItems.itemSnapshotList.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(50.dp))
                        }
                    }
                }
            }
        }

        if (lazyPagingItems.loadState.refresh is LoadState.Error) {
            GenericError(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MoSoTheme.colors.layer1),
                onRetryClicked = { lazyPagingItems.refresh() },
            )
        }
    }
}
