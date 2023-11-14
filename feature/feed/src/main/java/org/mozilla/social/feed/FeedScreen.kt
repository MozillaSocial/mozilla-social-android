@file:OptIn(ExperimentalMaterial3Api::class)

package org.mozilla.social.feed

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.foundation.Image
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOf
import org.koin.androidx.compose.koinViewModel
import org.mozilla.social.core.designsystem.component.MoSoAppBar
import org.mozilla.social.core.designsystem.component.MoSoSurface
import org.mozilla.social.core.designsystem.component.MoSoTab
import org.mozilla.social.core.designsystem.component.MoSoTabRow
import org.mozilla.social.core.designsystem.icon.mozillaLogo
import org.mozilla.social.core.designsystem.theme.MoSoTheme
import org.mozilla.social.core.ui.postcard.PostCardInteractions
import org.mozilla.social.core.ui.postcard.PostCardList

import org.mozilla.social.core.ui.postcard.PostCardUiState

@Composable
internal fun FeedScreen(
    viewModel: FeedViewModel = koinViewModel()
) {
    FeedScreen(
        homeFeed = viewModel.homeFeed,
        localFeed = viewModel.localFeed,
        federatedFeed = viewModel.federatedFeed,
        timelineTypeFlow = viewModel.timelineType,
        postCardInteractions = viewModel.postCardDelegate,
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
    postCardInteractions: PostCardInteractions,
    feedInteractions: FeedInteractions,
    topAppBarScrollBehavior: TopAppBarScrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(
        rememberTopAppBarState()
    ),
) {
    val selectedTimelineType = timelineTypeFlow.collectAsState().value
    val context = LocalContext.current

    MoSoSurface {
        Column(
            modifier = Modifier
                .nestedScroll(topAppBarScrollBehavior.nestedScrollConnection),
        ) {

            MoSoAppBar(
                scrollBehavior = topAppBarScrollBehavior,
                title = {
                    Image(
                        painter = mozillaLogo(),
                        contentDescription = "mozilla logo"
                    )
                },
                actions = {}
            )

            MoSoTabRow(
                selectedTabIndex = selectedTimelineType.ordinal,
                divider = {},
            ) {
                TimelineType.entries.forEach { timelineType ->
                    MoSoTab(
                        modifier = Modifier
                            .height(40.dp),
                        selected = selectedTimelineType == timelineType,
                        onClick = { feedInteractions.onTabClicked(timelineType) },
                        content = {
                            Text(
                                text = timelineType.tabTitle.build(context),
                                style = MoSoTheme.typography.labelMedium
                            )
                        },
                    )
                }
            }

            val forYouScrollState = rememberLazyListState()
            val localScrollState = rememberLazyListState()
            val federatedScrollState = rememberLazyListState()

            PostCardList(
                feed = when (selectedTimelineType) {
                    TimelineType.FOR_YOU -> homeFeed
                    TimelineType.LOCAL -> localFeed
                    TimelineType.FEDERATED -> federatedFeed
                },
                postCardInteractions = postCardInteractions,
                pullToRefreshEnabled = true,
                isFullScreenLoading = true,
                refreshSignalFlow = timelineTypeFlow,
                scrollState = when (selectedTimelineType) {
                    TimelineType.FOR_YOU -> forYouScrollState
                    TimelineType.LOCAL -> localScrollState
                    TimelineType.FEDERATED -> federatedScrollState
                }
            )
        }
    }
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, name = "Light theme")
private fun FeedScreenPreviewLight() {
    MoSoTheme(darkTheme = false) {
        FeedScreen(
            homeFeed = flowOf(),
            postCardInteractions = object : PostCardInteractions {},
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
            postCardInteractions = object : PostCardInteractions {},
            timelineTypeFlow = MutableStateFlow(TimelineType.FOR_YOU),
            feedInteractions = object : FeedInteractions {},
            localFeed = flowOf(),
            federatedFeed = flowOf(),
        )
    }
}