@file:OptIn(ExperimentalMaterial3Api::class)

package social.firefly.feed

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOf
import org.koin.androidx.compose.koinViewModel
import social.firefly.core.designsystem.font.MoSoFonts
import social.firefly.core.designsystem.theme.MoSoTheme
import social.firefly.core.ui.common.MoSoSurface
import social.firefly.core.ui.common.appbar.MoSoTopBar
import social.firefly.core.ui.common.pullrefresh.PullRefreshLazyColumn
import social.firefly.core.ui.common.tabs.MoSoTab
import social.firefly.core.ui.common.tabs.MoSoTabRow
import social.firefly.core.ui.common.text.LargeTextTitle
import social.firefly.core.ui.postcard.PostCardInteractions
import social.firefly.core.ui.postcard.PostCardUiState
import social.firefly.core.ui.postcard.postListContent
import social.firefly.feature.feed.R

@Composable
internal fun FeedScreen(viewModel: FeedViewModel = koinViewModel()) {
    FeedScreen(
        homeFeed = viewModel.homeFeed,
        localFeed = viewModel.localFeed,
        federatedFeed = viewModel.federatedFeed,
        timelineTypeFlow = viewModel.timelineType,
        homePostCardInteractions = viewModel.homePostCardDelegate,
        localPostCardInteractions = viewModel.localPostCardDelegate,
        federatedPostCardInteractions = viewModel.federatedPostCardDelegate,
        feedInteractions = viewModel,
    )

    LaunchedEffect(Unit) {
        viewModel.onScreenViewed()
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
    MoSoSurface {
        Column(
            modifier =
            Modifier
                .nestedScroll(topAppBarScrollBehavior.nestedScrollConnection),
        ) {
            MoSoTopBar(
                scrollBehavior = topAppBarScrollBehavior,
                title = {
                    LargeTextTitle(
                        text = stringResource(id = R.string.firefly),
                        fontSize = 24.sp,
                        fontWeight = FontWeight.W700,
                        fontFamily = MoSoFonts.petiteFormalScript,
                    )
                },
            )

            MainContent(
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
private fun MainContent(
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

    MoSoTabRow(
        selectedTabIndex = selectedTimelineType.ordinal,
        divider = {},
    ) {
        TimelineType.entries.forEach { timelineType ->
            MoSoTab(
                modifier =
                Modifier
                    .height(40.dp),
                selected = selectedTimelineType == timelineType,
                onClick = { feedInteractions.onTabClicked(timelineType) },
                content = {
                    Text(
                        text = timelineType.tabTitle.build(context),
                        style = MoSoTheme.typography.labelMedium,
                    )
                },
            )
        }
    }

    val forYouScrollState = rememberLazyListState()
    val localScrollState = rememberLazyListState()
    val federatedScrollState = rememberLazyListState()

    val homeFeedPagingItems = homeFeed.collectAsLazyPagingItems()
    val localFeedPagingItems = localFeed.collectAsLazyPagingItems()
    val federatedPagingItems = federatedFeed.collectAsLazyPagingItems()

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
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, name = "Light theme")
private fun FeedScreenPreviewLight() {
    MoSoTheme(darkTheme = false) {
        FeedScreen(
            homeFeed = flowOf(),
            homePostCardInteractions = object : PostCardInteractions {},
            localPostCardInteractions = object : PostCardInteractions {},
            federatedPostCardInteractions = object : PostCardInteractions {},
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
    MoSoTheme(darkTheme = true) {
        FeedScreen(
            homeFeed = flowOf(),
            homePostCardInteractions = object : PostCardInteractions {},
            localPostCardInteractions = object : PostCardInteractions {},
            federatedPostCardInteractions = object : PostCardInteractions {},
            timelineTypeFlow = MutableStateFlow(TimelineType.FOR_YOU),
            feedInteractions = object : FeedInteractions {},
            localFeed = flowOf(),
            federatedFeed = flowOf(),
        )
    }
}
