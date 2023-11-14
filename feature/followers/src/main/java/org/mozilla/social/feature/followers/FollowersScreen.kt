package org.mozilla.social.feature.followers

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import org.mozilla.social.core.designsystem.component.MoSoSurface
import org.mozilla.social.core.designsystem.component.MoSoTab
import org.mozilla.social.core.designsystem.component.MoSoTabRow
import org.mozilla.social.core.designsystem.theme.MoSoTheme
import org.mozilla.social.core.ui.common.account.quickview.AccountQuickView
import org.mozilla.social.core.ui.common.account.quickview.AccountQuickViewUiState
import org.mozilla.social.core.ui.common.appbar.MoSoCloseableTopAppBar
import org.mozilla.social.core.ui.common.error.GenericError
import org.mozilla.social.core.ui.common.pullrefresh.PullRefreshIndicator
import org.mozilla.social.core.ui.common.pullrefresh.pullRefresh
import org.mozilla.social.core.ui.common.pullrefresh.rememberPullRefreshState
import org.mozilla.social.core.ui.common.utils.PreviewTheme

@Composable
internal fun FollowersScreen(
    accountId: String,
    startingTab: FollowType,
    viewModel: FollowersViewModel = koinViewModel(
        parameters = {
            parametersOf(
                accountId,
            )
        }
    )
) {
    FollowersScreen(
        startingTab = startingTab,
        followers = viewModel.followers,
        following = viewModel.following,
        followersInteractions = viewModel,
    )

    LaunchedEffect(Unit) {
        viewModel.onScreenViewed()
    }
}

@Composable
private fun FollowersScreen(
    startingTab: FollowType,
    followers: Flow<PagingData<AccountQuickViewUiState>>,
    following: Flow<PagingData<AccountQuickViewUiState>>,
    followersInteractions: FollowersInteractions,
) {
    MoSoSurface {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
        ) {
            //TODO set title to user's name
            MoSoCloseableTopAppBar(
                title = "",
            )

            var selectedTab: FollowType by remember {
                mutableStateOf(startingTab)
            }

            MoSoTabRow(
                selectedTabIndex = selectedTab.ordinal,
            ) {
                FollowType.entries.forEach { tabType ->
                    MoSoTab(
                        modifier = Modifier
                            .height(40.dp),
                        selected = selectedTab == tabType,
                        onClick = { selectedTab = tabType },
                        content = {
                            Text(
                                text = when (tabType) {
                                    FollowType.FOLLOWERS ->
                                        stringResource(id = R.string.followers)
                                    FollowType.FOLLOWING ->
                                        stringResource(id = R.string.following)
                                },
                                style = MoSoTheme.typography.labelMedium
                            )
                        },
                    )
                }
            }

            FollowersList(
                list = when (selectedTab) {
                    FollowType.FOLLOWERS -> followers
                    FollowType.FOLLOWING -> following
                },
                followersInteractions = followersInteractions
            )
        }
    }
}

@Composable
private fun FollowersList(
    list: Flow<PagingData<AccountQuickViewUiState>>,
    followersInteractions: FollowersInteractions,
) {
    val lazyPagingItems = list.collectAsLazyPagingItems()

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

@Preview
@Composable
private fun FollowersScreenPreview() {
    PreviewTheme {
        FollowersScreen(
            startingTab = FollowType.FOLLOWERS,
            followers = flowOf(
                PagingData.from(
                    listOf(
                        AccountQuickViewUiState(
                            accountId = "",
                            displayName = "Person",
                            webFinger = "person",
                            avatarUrl = "",
                            isFollowing = false,
                        )
                    )
                )
            ),
            following = flowOf(),
            followersInteractions = object : FollowersInteractions {},
        )
    }
}