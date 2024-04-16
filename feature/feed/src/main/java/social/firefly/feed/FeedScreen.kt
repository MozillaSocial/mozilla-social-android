@file:OptIn(ExperimentalMaterial3Api::class)

package social.firefly.feed

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideIn
import androidx.compose.animation.slideOut
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import social.firefly.core.designsystem.font.FfFonts
import social.firefly.core.designsystem.icon.FfIcons
import social.firefly.core.designsystem.theme.FfTheme
import social.firefly.core.push.PushRegistration
import social.firefly.core.ui.common.FfSurface
import social.firefly.core.ui.common.appbar.FfTopBar
import social.firefly.core.ui.common.button.FfFloatingActionButton
import social.firefly.core.ui.common.pullrefresh.PullRefreshLazyColumn
import social.firefly.core.ui.common.tabs.FfTab
import social.firefly.core.ui.common.tabs.FfTabRow
import social.firefly.core.ui.common.text.LargeTextTitle
import social.firefly.core.ui.postcard.PostCardInteractions
import social.firefly.core.ui.postcard.PostCardInteractionsNoOp
import social.firefly.core.ui.postcard.PostCardUiState
import social.firefly.core.ui.postcard.postListContent
import social.firefly.feature.feed.R

@Composable
internal fun FeedScreen(viewModel: FeedViewModel = koinViewModel()) {

    val homeFeed = viewModel.homeFeed.collectAsStateWithLifecycle().value

    FeedScreen(
        homeFeed = homeFeed,
        localFeed = viewModel.localFeed,
        federatedFeed = viewModel.federatedFeed,
        timelineTypeFlow = viewModel.timelineType,
        homePostCardInteractions = viewModel.homePostCardDelegate,
        localPostCardInteractions = viewModel.localPostCardDelegate,
        federatedPostCardInteractions = viewModel.federatedPostCardDelegate,
        feedInteractions = viewModel,
    )

    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.onScreenViewed()
        PushRegistration.register(context)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FeedScreen(
    homeFeed: Flow<PagingData<PostCardUiState>>,
    localFeed: Flow<PagingData<PostCardUiState>>,
    federatedFeed: Flow<PagingData<PostCardUiState>>,
    timelineTypeFlow: StateFlow<TimelineType>,
    homePostCardInteractions: PostCardInteractions,
    localPostCardInteractions: PostCardInteractions,
    federatedPostCardInteractions: PostCardInteractions,
    feedInteractions: FeedInteractions,
    topAppBarScrollBehavior: TopAppBarScrollBehavior =
        TopAppBarDefaults.enterAlwaysScrollBehavior(
            rememberTopAppBarState(),
        ),
) {
    FfSurface {
        Column(
            modifier =
            Modifier
                .nestedScroll(topAppBarScrollBehavior.nestedScrollConnection),
        ) {
            FfTopBar(
                scrollBehavior = topAppBarScrollBehavior,
                title = {
                    LargeTextTitle(
                        text = stringResource(id = R.string.mozilla),
                        fontSize = 24.sp,
                        fontWeight = FontWeight.W700,
                        fontFamily = FfFonts.zillaSlab,
                    )
                },
            )

            TabbedContent(
                homeFeed = homeFeed,
                localFeed = localFeed,
                federatedFeed = federatedFeed,
                timelineTypeFlow = timelineTypeFlow,
                homePostCardInteractions = homePostCardInteractions,
                localPostCardInteractions = localPostCardInteractions,
                federatedPostCardInteractions = federatedPostCardInteractions,
                feedInteractions = feedInteractions,
            )
        }
    }
}

@Composable
private fun TabbedContent(
    homeFeed: Flow<PagingData<PostCardUiState>>,
    localFeed: Flow<PagingData<PostCardUiState>>,
    federatedFeed: Flow<PagingData<PostCardUiState>>,
    timelineTypeFlow: StateFlow<TimelineType>,
    homePostCardInteractions: PostCardInteractions,
    localPostCardInteractions: PostCardInteractions,
    federatedPostCardInteractions: PostCardInteractions,
    feedInteractions: FeedInteractions,
) {
    val selectedTimelineType by timelineTypeFlow.collectAsStateWithLifecycle()
    val context = LocalContext.current

    FfTabRow(
        selectedTabIndex = selectedTimelineType.ordinal,
        divider = {},
    ) {
        TimelineType.entries.forEach { timelineType ->
            FfTab(
                modifier =
                Modifier
                    .height(40.dp),
                selected = selectedTimelineType == timelineType,
                onClick = { feedInteractions.onTabClicked(timelineType) },
                content = {
                    Text(
                        text = timelineType.tabTitle.build(context),
                        style = FfTheme.typography.labelMedium,
                    )
                },
            )
        }
    }

    Box {
        MainContent(
            homeFeed = homeFeed,
            localFeed = localFeed,
            federatedFeed = federatedFeed,
            selectedTimelineType = selectedTimelineType,
            homePostCardInteractions = homePostCardInteractions,
            localPostCardInteractions = localPostCardInteractions,
            federatedPostCardInteractions = federatedPostCardInteractions,
            feedInteractions = feedInteractions,
        )
    }
}

@Composable
private fun BoxScope.MainContent(
    homeFeed: Flow<PagingData<PostCardUiState>>,
    localFeed: Flow<PagingData<PostCardUiState>>,
    federatedFeed: Flow<PagingData<PostCardUiState>>,
    selectedTimelineType: TimelineType,
    homePostCardInteractions: PostCardInteractions,
    localPostCardInteractions: PostCardInteractions,
    federatedPostCardInteractions: PostCardInteractions,
    feedInteractions: FeedInteractions,
) {
    val forYouScrollState = rememberLazyListState()
    val localScrollState = rememberLazyListState()
    val federatedScrollState = rememberLazyListState()

    val homeFeedPagingItems = homeFeed.collectAsLazyPagingItems()
    val localFeedPagingItems = localFeed.collectAsLazyPagingItems()
    val federatedPagingItems = federatedFeed.collectAsLazyPagingItems()

    HomeListener(
        forYouScrollState = forYouScrollState,
        homeFeedPagingItems = homeFeedPagingItems,
        feedInteractions = feedInteractions,
    )

    PullRefreshLazyColumn(
        lazyPagingItems = when (selectedTimelineType) {
            TimelineType.FOR_YOU -> homeFeedPagingItems
            TimelineType.LOCAL -> localFeedPagingItems
            TimelineType.FEDERATED -> federatedPagingItems
        },
        listState = when (selectedTimelineType) {
            TimelineType.FOR_YOU -> forYouScrollState
            TimelineType.LOCAL -> localScrollState
            TimelineType.FEDERATED -> federatedScrollState
        },
    ) {
        postListContent(
            lazyPagingItems = when (selectedTimelineType) {
                TimelineType.FOR_YOU -> homeFeedPagingItems
                TimelineType.LOCAL -> localFeedPagingItems
                TimelineType.FEDERATED -> federatedPagingItems
            },
            postCardInteractions = when (selectedTimelineType) {
                TimelineType.FOR_YOU -> homePostCardInteractions
                TimelineType.LOCAL -> localPostCardInteractions
                TimelineType.FEDERATED -> federatedPostCardInteractions
            },
        )
    }

    if (selectedTimelineType == TimelineType.FOR_YOU) {
        val showScrollUpButton by remember(forYouScrollState) {
            derivedStateOf { forYouScrollState.firstVisibleItemIndex > 1 }
        }
        val coroutineScope = rememberCoroutineScope()

        AnimatedVisibility(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            visible = showScrollUpButton,
            enter = slideIn {
                IntOffset(
                    x = 0,
                    y = 300
                )
            },
            exit = slideOut {
                IntOffset(
                    x = 0,
                    y = 300
                )
            }
        ) {
            FfFloatingActionButton(
                onClick = {
                    coroutineScope.launch {
                        forYouScrollState.scrollToItem(0, 0)
                    }
                    homeFeedPagingItems.refresh()
                }
            ) {
                Icon(
                    modifier = Modifier
                        .size(24.dp),
                    painter = FfIcons.arrowLineUp(),
                    contentDescription = stringResource(id = R.string.scroll_to_top_content_description),
                    tint = FfTheme.colors.textActionPrimary,
                )
            }
        }
    }
}

// Watches for the first visible item and sends the status ID to the viewmodel.
// Used for saving the last seen item so we can bring the user back there when they reopen the app.
@Composable
private fun HomeListener(
    forYouScrollState: LazyListState,
    homeFeedPagingItems: LazyPagingItems<PostCardUiState>,
    feedInteractions: FeedInteractions,
) {
    val forYouFirstVisibleIndex by remember(forYouScrollState) {
        derivedStateOf {
            // there seems to be a bug where 0 is not possible.
            // It's not perfect, but using 0 is better than 1 if we are not sure which is right
            if (forYouScrollState.firstVisibleItemIndex <= 1) {
                0
            } else {
                forYouScrollState.firstVisibleItemIndex
            }
        }
    }

    LaunchedEffect(forYouFirstVisibleIndex) {
        if (homeFeedPagingItems.itemCount != 0) {
            homeFeedPagingItems.peek(forYouFirstVisibleIndex)?.let { uiState ->
                feedInteractions.onStatusViewed(uiState.statusId)
            }
        }
    }
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, name = "Light theme")
private fun FeedScreenPreviewLight() {
    FfTheme(darkTheme = false) {
        FeedScreen(
            homeFeed = flowOf(),
            homePostCardInteractions = PostCardInteractionsNoOp,
            localPostCardInteractions = PostCardInteractionsNoOp,
            federatedPostCardInteractions = PostCardInteractionsNoOp,
            timelineTypeFlow = MutableStateFlow(TimelineType.FOR_YOU),
            feedInteractions = object : FeedInteractions {},
            localFeed = flowOf(),
            federatedFeed = flowOf(),
        )
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, name = "Dark theme")
@Composable
private fun FeedScreenPreviewDark() {
    FfTheme(darkTheme = true) {
        FeedScreen(
            homeFeed = flowOf(),
            homePostCardInteractions = PostCardInteractionsNoOp,
            localPostCardInteractions = PostCardInteractionsNoOp,
            federatedPostCardInteractions = PostCardInteractionsNoOp,
            timelineTypeFlow = MutableStateFlow(TimelineType.FOR_YOU),
            feedInteractions = object : FeedInteractions {},
            localFeed = flowOf(),
            federatedFeed = flowOf(),
        )
    }
}
