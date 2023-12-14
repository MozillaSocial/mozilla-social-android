@file:OptIn(ExperimentalMaterial3Api::class)

package org.mozilla.social.feed

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOf
import org.koin.androidx.compose.koinViewModel
import org.mozilla.social.core.designsystem.icon.MoSoIcons
import org.mozilla.social.core.designsystem.icon.mozillaLogo
import org.mozilla.social.core.designsystem.theme.MoSoTheme
import org.mozilla.social.core.ui.common.MoSoSurface
import org.mozilla.social.core.ui.common.MoSoTab
import org.mozilla.social.core.ui.common.MoSoTabRow
import org.mozilla.social.core.ui.common.appbar.MoSoAppBar
import org.mozilla.social.core.ui.postcard.PostCardInteractions
import org.mozilla.social.core.ui.postcard.PostCardList
import org.mozilla.social.core.ui.postcard.PostCardUiState

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
    val selectedTimelineType by timelineTypeFlow.collectAsStateWithLifecycle()
    val context = LocalContext.current

    MoSoSurface {
        Column(
            modifier =
                Modifier
                    .nestedScroll(topAppBarScrollBehavior.nestedScrollConnection),
        ) {
            MoSoAppBar(
                scrollBehavior = topAppBarScrollBehavior,
                title = {
                    Image(
                        painter = mozillaLogo(),
                        contentDescription = "mozilla logo",
                    )
                },
                actions = {
                    IconButton(
                        modifier = Modifier
                            .size(24.dp),
                        onClick = { feedInteractions.onSearchClicked() }
                    ) {
                        Icon(
                            modifier = Modifier.size(24.dp),
                            painter = MoSoIcons.magnifyingGlass(),
                            contentDescription = "",
                            tint = MoSoTheme.colors.iconPrimary
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                },
            )

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

            PostCardList(
                feed =
                    when (selectedTimelineType) {
                        TimelineType.FOR_YOU -> homeFeed
                        TimelineType.LOCAL -> localFeed
                        TimelineType.FEDERATED -> federatedFeed
                    },
                postCardInteractions = when (selectedTimelineType) {
                    TimelineType.FOR_YOU -> homePostCardInteractions
                    TimelineType.LOCAL -> localPostCardInteractions
                    TimelineType.FEDERATED -> federatedPostCardInteractions
                },
                pullToRefreshEnabled = true,
                isFullScreenLoading = true,
                refreshSignalFlow = timelineTypeFlow,
                scrollState =
                    when (selectedTimelineType) {
                        TimelineType.FOR_YOU -> forYouScrollState
                        TimelineType.LOCAL -> localScrollState
                        TimelineType.FEDERATED -> federatedScrollState
                    },
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
