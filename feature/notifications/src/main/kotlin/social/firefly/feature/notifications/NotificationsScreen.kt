package social.firefly.feature.notifications

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import org.koin.androidx.compose.koinViewModel
import social.firefly.core.designsystem.theme.FfSpacing
import social.firefly.core.ui.common.FfSurface
import social.firefly.core.ui.common.appbar.FfTopBar
import social.firefly.core.ui.common.divider.FfDivider
import social.firefly.core.ui.common.pullrefresh.PullRefreshLazyColumn
import social.firefly.core.ui.common.pullrefresh.rememberPullRefreshState
import social.firefly.core.ui.common.tabs.FfTab
import social.firefly.core.ui.common.tabs.FfTabRow
import social.firefly.core.ui.common.text.MediumTextLabel
import social.firefly.core.ui.common.utils.PreviewTheme
import social.firefly.core.ui.notifications.NotificationCard
import social.firefly.core.ui.notifications.NotificationInteractions
import social.firefly.core.ui.notifications.NotificationInteractionsNoOp
import social.firefly.core.ui.notifications.NotificationUiState
import social.firefly.core.ui.postcard.PostCardInteractions
import social.firefly.core.ui.postcard.PostCardInteractionsNoOp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun NotificationsScreen(
    viewModel: NotificationsViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    NotificationsScreen(
        uiState = uiState,
        feed = viewModel.feed,
        mentionFeed = viewModel.mentionsFeed,
        followsFeed = viewModel.followsFeed,
        notificationsInteractions = viewModel,
        postCardInteractions = viewModel.postCardDelegate,
        notificationInteractions = viewModel.notificationCardDelegate,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun NotificationsScreen(
    uiState: NotificationsUiState,
    feed: Flow<PagingData<NotificationUiState>>,
    mentionFeed: Flow<PagingData<NotificationUiState>>,
    followsFeed: Flow<PagingData<NotificationUiState>>,
    notificationsInteractions: NotificationsInteractions,
    postCardInteractions: PostCardInteractions,
    notificationInteractions: NotificationInteractions,
    topAppBarScrollBehavior: TopAppBarScrollBehavior =
        TopAppBarDefaults.enterAlwaysScrollBehavior(
            rememberTopAppBarState(),
        ),
) {
    FfSurface(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
    ) {
        Column(
            modifier = Modifier
                .nestedScroll(topAppBarScrollBehavior.nestedScrollConnection)
        ) {
            FfTopBar(
                scrollBehavior = topAppBarScrollBehavior,
                title = stringResource(id = R.string.notifications_title),
                icon = null,
                onIconClicked = {},
                showDivider = false,
            )
            Tabs(
                uiState = uiState,
                notificationsInteractions = notificationsInteractions
            )

            val all = feed.collectAsLazyPagingItems()
            val mentions = mentionFeed.collectAsLazyPagingItems()
            val follows = followsFeed.collectAsLazyPagingItems()

            val allListState = rememberLazyListState()
            val mentionsListState = rememberLazyListState()
            val followsListState = rememberLazyListState()

            NotificationsList(
                lazyPagingItems = when (uiState.selectedTab) {
                    NotificationsTab.ALL -> all
                    NotificationsTab.MENTIONS -> mentions
                    NotificationsTab.REQUESTS -> follows
                },
                listState = when (uiState.selectedTab) {
                    NotificationsTab.ALL -> allListState
                    NotificationsTab.MENTIONS -> mentionsListState
                    NotificationsTab.REQUESTS -> followsListState
                },
                postCardInteractions = postCardInteractions,
                notificationInteractions = notificationInteractions,
            )
        }
    }
}

@Composable
private fun Tabs(
    uiState: NotificationsUiState,
    notificationsInteractions: NotificationsInteractions,
) {
    val context = LocalContext.current

    FfTabRow(selectedTabIndex = uiState.selectedTab.ordinal) {
        NotificationsTab.entries.forEach { tabType ->
            if (tabType == NotificationsTab.REQUESTS && !uiState.requestsTabIsVisible) {
                return@forEach
            }
            FfTab(
                modifier =
                Modifier
                    .height(40.dp),
                selected = uiState.selectedTab == tabType,
                onClick = { notificationsInteractions.onTabClicked(tabType) },
                content = {
                    MediumTextLabel(text = tabType.tabTitle.build(context))
                },
            )
        }
    }
}

@Composable
private fun NotificationsList(
    lazyPagingItems: LazyPagingItems<NotificationUiState>,
    listState: LazyListState,
    postCardInteractions: PostCardInteractions,
    notificationInteractions: NotificationInteractions,
) {
    val pullRefreshState =
        rememberPullRefreshState(
            refreshing = lazyPagingItems.loadState.refresh == LoadState.Loading,
            onRefresh = { lazyPagingItems.refresh() },
        )

    PullRefreshLazyColumn(
        lazyPagingItems = lazyPagingItems,
        pullRefreshState = pullRefreshState,
        modifier = Modifier.fillMaxSize(),
        listState = listState,
    ) {
        when (lazyPagingItems.loadState.refresh) {
            is LoadState.Error -> {} // handle the error outside the lazy column
            else ->
                items(
                    count = lazyPagingItems.itemCount,
                    key = lazyPagingItems.itemKey { it.id },
                ) { index ->
                    lazyPagingItems[index]?.let { uiState ->
                        Column {
                            NotificationCard(
                                modifier = Modifier.padding(FfSpacing.md),
                                uiState = uiState,
                                postCardInteractions = postCardInteractions,
                                notificationInteractions = notificationInteractions,
                            )
                            FfDivider()
                        }
                    }
                }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun NotificationsScreenPreview() {
    PreviewTheme {
        NotificationsScreen(
            uiState = NotificationsUiState(
                selectedTab = NotificationsTab.ALL,
            ),
            feed = flowOf(),
            mentionFeed = flowOf(),
            followsFeed = flowOf(),
            notificationsInteractions = object : NotificationsInteractions {
                override fun onTabClicked(tab: NotificationsTab) = Unit
            },
            postCardInteractions = PostCardInteractionsNoOp,
            notificationInteractions = NotificationInteractionsNoOp,
        )
    }
}