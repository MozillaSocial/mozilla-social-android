package org.mozilla.social.feed

import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOf
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import org.mozilla.social.common.utils.StringFactory
import org.mozilla.social.core.designsystem.component.MoSoSurface
import org.mozilla.social.core.designsystem.component.MoSoTab
import org.mozilla.social.core.designsystem.component.MoSoTabRow
import org.mozilla.social.core.designsystem.theme.MoSoTheme
import org.mozilla.social.core.ui.R
import org.mozilla.social.core.ui.postcard.PostCardInteractions
import org.mozilla.social.core.ui.postcard.PostCardList
import org.mozilla.social.core.ui.postcard.PostCardNavigation
import org.mozilla.social.core.ui.postcard.PostCardUiState
import org.mozilla.social.core.ui.recommendations.MoreInfoDialog

@Composable
internal fun FeedScreen(
    postCardNavigation: PostCardNavigation,
    viewModel: FeedViewModel = koinViewModel(parameters = {
        parametersOf(
            postCardNavigation
        )
    })
) {
    FeedScreen(
        feed = viewModel.feed,
        timelineTypeFlow = viewModel.timelineType,
        errorToastMessage = viewModel.postCardDelegate.errorToastMessage,
        postCardInteractions = viewModel.postCardDelegate,
        feedInteractions = viewModel,
    )
}

@Composable
private fun FeedScreen(
    feed: Flow<PagingData<PostCardUiState>>,
    timelineTypeFlow: StateFlow<TimelineType>,
    errorToastMessage: SharedFlow<StringFactory>,
    postCardInteractions: PostCardInteractions,
    feedInteractions: FeedInteractions,
) {
    val selectedTimelineType = timelineTypeFlow.collectAsState().value
    val context = LocalContext.current

    MoSoSurface {
        Column(
            modifier = Modifier.padding(horizontal = 4.dp)
        ) {
            MoSoTabRow(
                modifier = Modifier.padding(top = 20.dp),
                selectedTabIndex = selectedTimelineType.ordinal,
                divider = {},
            ) {
                TimelineType.values().forEach { timelineType ->
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

            PostCardList(
                feed = feed,
                errorToastMessage = errorToastMessage,
                postCardInteractions = postCardInteractions,
                pullToRefreshEnabled = true,
                isFullScreenLoading = true,
                refreshSignalFlow = timelineTypeFlow,
            )
        }
    }
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, name = "Light theme")
private fun FeedScreenPreviewLight() {
    MoSoTheme(darkTheme = false) {
        FeedScreen(
            feed = flowOf(),
            errorToastMessage = MutableSharedFlow(),
            postCardInteractions = object : PostCardInteractions {},
            timelineTypeFlow = MutableStateFlow(TimelineType.FOR_YOU),
            feedInteractions = object : FeedInteractions {},
        )
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, name = "Dark theme")
@Composable
private fun FeedScreenPreviewDark() {
    MoSoTheme(darkTheme = true) {
        FeedScreen(
            feed = flowOf(),
            errorToastMessage = MutableSharedFlow(),
            postCardInteractions = object : PostCardInteractions {},
            timelineTypeFlow = MutableStateFlow(TimelineType.FOR_YOU),
            feedInteractions = object : FeedInteractions {},
        )
    }
}