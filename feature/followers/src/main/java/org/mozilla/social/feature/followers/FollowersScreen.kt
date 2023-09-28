package org.mozilla.social.feature.followers

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import kotlinx.coroutines.flow.Flow
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import org.mozilla.social.core.designsystem.component.MoSoDivider
import org.mozilla.social.core.designsystem.component.MoSoTopBar
import org.mozilla.social.core.designsystem.theme.MoSoColors
import org.mozilla.social.core.designsystem.theme.MoSoTheme
import org.mozilla.social.core.ui.account.quickview.AccountQuickView
import org.mozilla.social.core.ui.account.quickview.AccountQuickViewUiState
import org.mozilla.social.core.ui.error.GenericError
import org.mozilla.social.core.ui.pullrefresh.PullRefreshIndicator
import org.mozilla.social.core.ui.pullrefresh.pullRefresh
import org.mozilla.social.core.ui.pullrefresh.rememberPullRefreshState

@Composable
internal fun FollowersRoute(
    accountId: String,
    followersNavigationCallbacks: FollowersNavigationCallbacks,
    viewModel: FollowersViewModel = koinViewModel(
        parameters = { parametersOf(
            accountId,
            followersNavigationCallbacks,
        ) }
    )
) {
    FollowersScreen(
        followers = viewModel.followers,
        followersInteractions = viewModel,
    )
}

@Composable
private fun FollowersScreen(
    followers: Flow<PagingData<AccountQuickViewUiState>>,
    followersInteractions: FollowersInteractions,
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        MoSoTopBar(
            title = stringResource(id = R.string.followers),
            onIconClicked = { followersInteractions.onCloseClicked() }
        )

        val lazyPagingItems = followers.collectAsLazyPagingItems()

        val pullRefreshState = rememberPullRefreshState(
            refreshing = lazyPagingItems.loadState.refresh == LoadState.Loading,
            onRefresh = { lazyPagingItems.refresh() }
        )

        Box(
            modifier = Modifier
                .pullRefresh(pullRefreshState)
                .fillMaxSize()
        ) {

            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                when (lazyPagingItems.loadState.refresh) {
                    is LoadState.Error -> {} // handle the error outside the lazy column
                    else -> items(
                        count = lazyPagingItems.itemCount,
                        key = lazyPagingItems.itemKey { it.accountId }
                    ) { index ->
                        lazyPagingItems[index]?.let { uiState ->
                            AccountQuickView(
                                uiState = uiState,
                                onClick = followersInteractions::onAccountClicked
                            )
                            MoSoDivider()
                        }
                    }
                }

                when (lazyPagingItems.loadState.append) {
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
                                onRetryClicked = { lazyPagingItems.retry() }
                            )
                        }
                    }

                    is LoadState.NotLoading -> {
                        item {
                            Text(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .wrapContentWidth(Alignment.CenterHorizontally)
                                    .padding(16.dp),
                                text = stringResource(id = R.string.end_of_the_list)
                            )
                        }
                    }
                }
            }

            if (lazyPagingItems.loadState.refresh is LoadState.Error) {
                GenericError(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MoSoTheme.colors.layer1),
                    onRetryClicked = { lazyPagingItems.refresh() }
                )
            }

            PullRefreshIndicator(
                modifier = Modifier.align(Alignment.TopCenter),
                refreshing = lazyPagingItems.loadState.refresh == LoadState.Loading,
                state = pullRefreshState,
            )
        }
    }
}