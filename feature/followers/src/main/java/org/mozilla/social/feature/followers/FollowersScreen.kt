package org.mozilla.social.feature.followers

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import org.mozilla.social.core.designsystem.theme.MoSoSpacing
import org.mozilla.social.core.designsystem.theme.MoSoTheme
import org.mozilla.social.core.navigation.navigationModule
import org.mozilla.social.core.ui.accountfollower.AccountFollower
import org.mozilla.social.core.ui.accountfollower.AccountFollowerUiState
import org.mozilla.social.core.ui.common.MoSoSurface
import org.mozilla.social.core.ui.common.tabs.MoSoTab
import org.mozilla.social.core.ui.common.tabs.MoSoTabRow
import org.mozilla.social.core.ui.common.account.quickview.AccountQuickViewUiState
import org.mozilla.social.core.ui.common.appbar.MoSoCloseableTopAppBar
import org.mozilla.social.core.ui.common.pullrefresh.PullRefreshLazyColumn
import org.mozilla.social.core.ui.common.pullrefresh.rememberPullRefreshState
import org.mozilla.social.core.ui.common.utils.PreviewTheme

@Composable
internal fun FollowersScreen(
    accountId: String,
    displayName: String,
    startingTab: FollowType,
    viewModel: FollowersViewModel =
        koinViewModel(
            parameters = {
                parametersOf(
                    accountId,
                )
            },
        ),
) {
    FollowersScreen(
        displayName = displayName,
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
    displayName: String,
    startingTab: FollowType,
    followers: Flow<PagingData<AccountFollowerUiState>>,
    following: Flow<PagingData<AccountFollowerUiState>>,
    followersInteractions: FollowersInteractions,
) {
    MoSoSurface {
        Column(
            modifier =
            Modifier
                .fillMaxSize()
                .systemBarsPadding(),
        ) {
            MoSoCloseableTopAppBar(
                title = displayName,
            )

            var selectedTab: FollowType by remember {
                mutableStateOf(startingTab)
            }

            MoSoTabRow(
                selectedTabIndex = selectedTab.ordinal,
            ) {
                FollowType.entries.forEach { tabType ->
                    MoSoTab(
                        modifier =
                        Modifier
                            .height(40.dp),
                        selected = selectedTab == tabType,
                        onClick = {
                            followersInteractions.onTabClicked(tabType)
                            selectedTab = tabType
                        },
                        content = {
                            Text(
                                text =
                                when (tabType) {
                                    FollowType.FOLLOWERS ->
                                        stringResource(id = R.string.followers)

                                    FollowType.FOLLOWING ->
                                        stringResource(id = R.string.following)
                                },
                                style = MoSoTheme.typography.labelMedium,
                            )
                        },
                    )
                }
            }

            val followersListState = rememberLazyListState()
            val followingListState = rememberLazyListState()

            FollowersList(
                list =
                when (selectedTab) {
                    FollowType.FOLLOWERS -> followers
                    FollowType.FOLLOWING -> following
                },
                listState = when (selectedTab) {
                    FollowType.FOLLOWERS -> followersListState
                    FollowType.FOLLOWING -> followingListState
                },
                followersInteractions = followersInteractions,
            )
        }
    }
}

@Composable
private fun FollowersList(
    list: Flow<PagingData<AccountFollowerUiState>>,
    listState: LazyListState,
    followersInteractions: FollowersInteractions,
) {
    val lazyPagingItems = list.collectAsLazyPagingItems()

    val pullRefreshState =
        rememberPullRefreshState(
            refreshing = lazyPagingItems.loadState.refresh == LoadState.Loading,
            onRefresh = { lazyPagingItems.refresh() },
        )

    PullRefreshLazyColumn(
        modifier = Modifier.fillMaxSize(),
        lazyPagingItems = lazyPagingItems,
        pullRefreshState = pullRefreshState,
        listState = listState,
    ) {
        when (lazyPagingItems.loadState.refresh) {
            is LoadState.Error -> {} // handle the error outside the lazy column
            else ->
                items(
                    count = lazyPagingItems.itemCount,
                    key = lazyPagingItems.itemKey { it.accountQuickViewUiState.accountId },
                ) { index ->
                    lazyPagingItems[index]?.let { uiState ->
                        AccountFollower(
                            uiState = uiState,
                            onButtonClicked = {
                                followersInteractions.onFollowClicked(
                                    uiState.accountQuickViewUiState.accountId,
                                    isFollowing = uiState.isFollowing,
                                )
                            },
                            modifier = Modifier
                                .padding(MoSoSpacing.md)
                                .clickable {
                                    followersInteractions
                                        .onAccountClicked(
                                            accountId = uiState.accountQuickViewUiState.accountId
                                        )
                                },
                        )
                    }
                }
        }
    }
}

@Preview
@Composable
private fun FollowersScreenPreview() {
    PreviewTheme(
        modules = listOf(navigationModule),
    ) {
        FollowersScreen(
            displayName = "Person",
            startingTab = FollowType.FOLLOWERS,
            followers =
            flowOf(
                PagingData.from(
                    listOf(
                        AccountFollowerUiState(
                            accountQuickViewUiState = AccountQuickViewUiState(
                                accountId = "",
                                displayName = "Person",
                                webFinger = "person",
                                avatarUrl = "",
                            ),
                            isFollowing = false,
                            bioHtml = "",
                            followButtonVisible = true,
                        )
                    ),
                ),
            ),
            following = flowOf(),
            followersInteractions = object : FollowersInteractions {},
        )
    }
}
