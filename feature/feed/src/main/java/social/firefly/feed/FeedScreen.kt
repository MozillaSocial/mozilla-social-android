@file:OptIn(ExperimentalMaterial3Api::class)

package social.firefly.feed

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideIn
import androidx.compose.animation.slideOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import kotlinx.coroutines.flow.Flow
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

    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value

    FeedScreen(
        uiState = uiState,
        homeFeed = uiState.homeFeed,
        localFeed = viewModel.localFeed,
        federatedFeed = viewModel.federatedFeed,
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
    uiState: FeedUiState,
    homeFeed: Flow<PagingData<PostCardUiState>>,
    localFeed: Flow<PagingData<PostCardUiState>>,
    federatedFeed: Flow<PagingData<PostCardUiState>>,
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
                uiState = uiState,
                homeFeed = homeFeed,
                localFeed = localFeed,
                federatedFeed = federatedFeed,
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
    uiState: FeedUiState,
    homeFeed: Flow<PagingData<PostCardUiState>>,
    localFeed: Flow<PagingData<PostCardUiState>>,
    federatedFeed: Flow<PagingData<PostCardUiState>>,
    homePostCardInteractions: PostCardInteractions,
    localPostCardInteractions: PostCardInteractions,
    federatedPostCardInteractions: PostCardInteractions,
    feedInteractions: FeedInteractions,
) {
    val context = LocalContext.current

    FfTabRow(
        selectedTabIndex = uiState.timelineType.ordinal,
        divider = {},
    ) {
        TimelineType.entries.forEach { timelineType ->
            FfTab(
                modifier =
                Modifier
                    .height(40.dp),
                selected = uiState.timelineType == timelineType,
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

    MainContent(
        uiState = uiState,
        homeFeed = homeFeed,
        localFeed = localFeed,
        federatedFeed = federatedFeed,
        homePostCardInteractions = homePostCardInteractions,
        localPostCardInteractions = localPostCardInteractions,
        federatedPostCardInteractions = federatedPostCardInteractions,
        feedInteractions = feedInteractions,
    )
}

@Composable
private fun MainContent(
    uiState: FeedUiState,
    homeFeed: Flow<PagingData<PostCardUiState>>,
    localFeed: Flow<PagingData<PostCardUiState>>,
    federatedFeed: Flow<PagingData<PostCardUiState>>,
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

    Box {
        PullRefreshLazyColumn(
            lazyPagingItems = when (uiState.timelineType) {
                TimelineType.FOR_YOU -> homeFeedPagingItems
                TimelineType.LOCAL -> localFeedPagingItems
                TimelineType.FEDERATED -> federatedPagingItems
            },
            listState = when (uiState.timelineType) {
                TimelineType.FOR_YOU -> forYouScrollState
                TimelineType.LOCAL -> localScrollState
                TimelineType.FEDERATED -> federatedScrollState
            },
        ) {
            postListContent(
                lazyPagingItems = when (uiState.timelineType) {
                    TimelineType.FOR_YOU -> homeFeedPagingItems
                    TimelineType.LOCAL -> localFeedPagingItems
                    TimelineType.FEDERATED -> federatedPagingItems
                },
                postCardInteractions = when (uiState.timelineType) {
                    TimelineType.FOR_YOU -> homePostCardInteractions
                    TimelineType.LOCAL -> localPostCardInteractions
                    TimelineType.FEDERATED -> federatedPostCardInteractions
                },
            )
        }

        ScrollUpButton(
            uiState = uiState,
            forYouScrollState = forYouScrollState,
            homeFeedPagingItems = homeFeedPagingItems,
            feedInteractions = feedInteractions
        )
    }
}

@Composable
private fun BoxScope.ScrollUpButton(
    uiState: FeedUiState,
    forYouScrollState: LazyListState,
    homeFeedPagingItems: LazyPagingItems<PostCardUiState>,
    feedInteractions: FeedInteractions,
) {
    if (uiState.timelineType == TimelineType.FOR_YOU) {
        ScrollWatcher(
            forYouScrollState = forYouScrollState,
            homeFeedPagingItems = homeFeedPagingItems,
            feedInteractions = feedInteractions,
        )

        val coroutineScope = rememberCoroutineScope()

        AnimatedVisibility(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            visible = uiState.scrollUpButtonVisible,
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
                        feedInteractions.onScrollToTopClicked {
                            homeFeedPagingItems.refresh()
                            forYouScrollState.scrollToItem(0, 0)
                        }
                    }
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

@Composable
private fun ScrollWatcher(
    forYouScrollState: LazyListState,
    homeFeedPagingItems: LazyPagingItems<PostCardUiState>,
    feedInteractions: FeedInteractions,
) {
    val firstVisibleIndex by remember(forYouScrollState) {
        derivedStateOf { forYouScrollState.firstVisibleItemIndex }
    }

    LaunchedEffect(firstVisibleIndex) {
        println("johnny launch")
        if (homeFeedPagingItems.itemCount != 0) {
            homeFeedPagingItems.peek(firstVisibleIndex)?.let { uiState ->
                feedInteractions.onFirstVisibleItemIndexForHomeChanged(
                    index = firstVisibleIndex,
                    statusId = uiState.statusId
                )
            }
        }
    }

    LaunchedEffect(homeFeedPagingItems.loadState.prepend.endOfPaginationReached) {
        feedInteractions.onHomePrependEndReached(
            homeFeedPagingItems.loadState.prepend.endOfPaginationReached
        )
    }
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, name = "Light theme")
private fun FeedScreenPreviewLight() {
    FfTheme(darkTheme = false) {
        FeedScreen(
            uiState = FeedUiState(),
            homeFeed = flowOf(),
            homePostCardInteractions = PostCardInteractionsNoOp,
            localPostCardInteractions = PostCardInteractionsNoOp,
            federatedPostCardInteractions = PostCardInteractionsNoOp,
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
            uiState = FeedUiState(),
            homeFeed = flowOf(),
            homePostCardInteractions = PostCardInteractionsNoOp,
            localPostCardInteractions = PostCardInteractionsNoOp,
            federatedPostCardInteractions = PostCardInteractionsNoOp,
            feedInteractions = object : FeedInteractions {},
            localFeed = flowOf(),
            federatedFeed = flowOf(),
        )
    }
}
