@file:OptIn(ExperimentalMaterial3Api::class)

package social.firefly.feed

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideIn
import androidx.compose.animation.slideOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
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
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import social.firefly.common.utils.painterResourceFactory
import social.firefly.core.designsystem.font.FfFonts
import social.firefly.core.designsystem.icon.FfIcons
import social.firefly.core.designsystem.theme.FfTheme
import social.firefly.core.designsystem.theme.ThemeOption
import social.firefly.core.push.PushRegistration
import social.firefly.core.ui.common.FfSurface
import social.firefly.core.ui.common.appbar.FfTopBar
import social.firefly.core.ui.common.button.FfButtonSecondary
import social.firefly.core.ui.common.button.FfFloatingActionButton
import social.firefly.core.ui.common.pullrefresh.PullRefreshLazyColumn
import social.firefly.core.ui.common.tabs.FfTab
import social.firefly.core.ui.common.tabs.FfTabRow
import social.firefly.core.ui.common.text.LargeTextTitle
import social.firefly.core.ui.common.text.MediumTextBody
import social.firefly.core.ui.common.text.MediumTextLabel
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
        homeFeed = viewModel.homeFeed,
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
        when (uiState.timelineType) {
            TimelineType.FOR_YOU -> {
                PullRefreshLazyColumn(
                    lazyPagingItems = homeFeedPagingItems,
                    emptyListContent = {
                        ForYouEmptyState(
                            feedInteractions = feedInteractions,
                        )
                    },
                    listState = forYouScrollState,
                ) {
                    postListContent(
                        lazyPagingItems = homeFeedPagingItems,
                        postCardInteractions = homePostCardInteractions,
                    )
                }
            }

            TimelineType.LOCAL -> {
                PullRefreshLazyColumn(
                    lazyPagingItems = localFeedPagingItems,
                    listState = localScrollState,
                ) {
                    postListContent(
                        lazyPagingItems = localFeedPagingItems,
                        postCardInteractions = localPostCardInteractions,
                    )
                }
            }
            TimelineType.FEDERATED -> {
                PullRefreshLazyColumn(
                    lazyPagingItems = federatedPagingItems,
                    listState = federatedScrollState,
                ) {
                    postListContent(
                        lazyPagingItems = federatedPagingItems,
                        postCardInteractions = federatedPostCardInteractions,
                    )
                }
            }
        }

        //TODO maybe try enabling this again some day.  Seems to be some weird bugs with
        // the list when prepending data.
//        ScrollUpButton(
//            uiState = uiState,
//            forYouScrollState = forYouScrollState,
//            homeFeedPagingItems = homeFeedPagingItems,
//            feedInteractions = feedInteractions
//        )
    }
}

@Composable
private fun ForYouEmptyState(
    feedInteractions: FeedInteractions,
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .width(400.dp)
                .padding(16.dp),
        ) {
            LargeTextTitle(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                text = stringResource(id = R.string.empty_home_title)
            )

            Image(
                modifier = Modifier.fillMaxWidth(),
                painter = painterResource(R.drawable.tree),
                contentDescription = null,
                contentScale = ContentScale.FillWidth,
            )

            MediumTextBody(text = stringResource(id = R.string.empty_home_body))

            Spacer(modifier = Modifier.height(16.dp))

            FfButtonSecondary(
                modifier = Modifier.fillMaxWidth(),
                onClick = { feedInteractions.onGoToDiscoverClicked() }
            ) {
                MediumTextLabel(text = stringResource(id = R.string.empty_home_trending_and_search))
            }

            Spacer(modifier = Modifier.height(16.dp))

            MediumTextBody(text = stringResource(id = R.string.empty_home_body_2))

            Spacer(modifier = Modifier.height(16.dp))

            FfButtonSecondary(
                modifier = Modifier.fillMaxWidth(),
                onClick = { feedInteractions.onTabClicked(TimelineType.LOCAL) }
            ) {
                MediumTextLabel(text = stringResource(id = R.string.empty_home_local_button))
            }

            Spacer(modifier = Modifier.height(16.dp))

            FfButtonSecondary(
                modifier = Modifier.fillMaxWidth(),
                onClick = { feedInteractions.onTabClicked(TimelineType.FEDERATED) }
            ) {
                MediumTextLabel(text = stringResource(id = R.string.empty_home_fediverse_button))
            }
        }
    }
}

@Suppress("UnusedPrivateMember")
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
    LaunchedEffect(forYouScrollState) {
        snapshotFlow { forYouScrollState.firstVisibleItemIndex }
            .distinctUntilChanged()
            .collect { firstVisibleIndex ->
                if (homeFeedPagingItems.itemCount != 0) {
                    homeFeedPagingItems.peek(firstVisibleIndex)?.let { uiState ->
                        feedInteractions.onFirstVisibleItemIndexForHomeChanged(
                            index = firstVisibleIndex,
                            statusId = uiState.statusId
                        )
                    }
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
    FfTheme {
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
    FfTheme(ThemeOption.DARK) {
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
