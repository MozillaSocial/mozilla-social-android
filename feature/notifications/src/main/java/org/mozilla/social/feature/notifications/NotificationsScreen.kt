package org.mozilla.social.feature.notifications

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import kotlinx.coroutines.flow.Flow
import org.koin.androidx.compose.koinViewModel
import org.mozilla.social.core.ui.common.MoSoSurface
import org.mozilla.social.core.ui.common.MoSoTab
import org.mozilla.social.core.ui.common.MoSoTabRow
import org.mozilla.social.core.ui.common.appbar.MoSoTopBar
import org.mozilla.social.core.ui.common.divider.MoSoDivider
import org.mozilla.social.core.ui.common.pullrefresh.PullRefreshLazyColumn
import org.mozilla.social.core.ui.common.pullrefresh.rememberPullRefreshState
import org.mozilla.social.core.ui.common.text.MediumTextLabel
import org.mozilla.social.core.ui.notifications.NotificationCard
import org.mozilla.social.core.ui.notifications.NotificationUiState

@Composable
internal fun NotificationsScreen(
    viewModel: NotificationsViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    NotificationsScreen(
        uiState = uiState,
        feed = viewModel.feed,
        notificationsInteractions = viewModel,
    )
}

@Composable
private fun NotificationsScreen(
    uiState: NotificationsUiState,
    feed: Flow<PagingData<NotificationUiState>>,
    notificationsInteractions: NotificationsInteractions,
) {
    MoSoSurface(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
    ) {
        Column {
            MoSoTopBar(
                title = stringResource(id = R.string.notifications_title),
                icon = null,
                onIconClicked = {},
                showDivider = false,
            )
            Tabs(
                uiState = uiState,
                notificationsInteractions = notificationsInteractions
            )
            NotificationsList(list = feed)
        }
    }
}

@Composable
private fun Tabs(
    uiState: NotificationsUiState,
    notificationsInteractions: NotificationsInteractions,
) {
    val context = LocalContext.current

    MoSoTabRow(selectedTabIndex = uiState.selectedTab.ordinal) {
        NotificationsTab.entries.forEach { tabType ->
            MoSoTab(
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
    list: Flow<PagingData<NotificationUiState>>,
) {
    val lazyPagingItems = list.collectAsLazyPagingItems()

    val pullRefreshState =
        rememberPullRefreshState(
            refreshing = lazyPagingItems.loadState.refresh == LoadState.Loading,
            onRefresh = { lazyPagingItems.refresh() },
        )

    PullRefreshLazyColumn(
        lazyPagingItems,
        pullRefreshState = pullRefreshState,
        modifier = Modifier.fillMaxSize(),
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
                            NotificationCard(uiState = uiState)
                            MoSoDivider()
                        }
                    }
                }
        }
    }
}