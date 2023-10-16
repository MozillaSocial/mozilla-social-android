package org.mozilla.social.feed

import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.flowOf
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import org.mozilla.social.common.utils.StringFactory
import org.mozilla.social.core.designsystem.component.MoSoSurface
import org.mozilla.social.core.designsystem.theme.MoSoTheme
import org.mozilla.social.core.ui.LazyListStateKey
import org.mozilla.social.core.ui.R
import org.mozilla.social.core.ui.postcard.PostCardInteractions
import org.mozilla.social.core.ui.postcard.PostCardList
import org.mozilla.social.core.ui.postcard.PostCardNavigation
import org.mozilla.social.core.ui.postcard.PostCardUiState
import org.mozilla.social.core.ui.recommendations.MoreInfoDialog
import org.mozilla.social.core.ui.recommendations.RecommendationCarousel
import org.mozilla.social.model.Recommendation

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
        errorToastMessage = viewModel.postCardDelegate.errorToastMessage,
        postCardInteractions = viewModel.postCardDelegate,
        reccs = viewModel.reccs.collectAsState(initial = emptyList()).value
    )
}

@Composable
private fun FeedScreen(
    feed: Flow<PagingData<PostCardUiState>>,
    errorToastMessage: SharedFlow<StringFactory>,
    reccs: List<Recommendation>? = null,
    postCardInteractions: PostCardInteractions,
) {
    MoSoSurface {
        Box(
            modifier = Modifier.padding(horizontal = 4.dp)
        ) {

            val openAlertDialog = remember { mutableStateOf(false) }

            if (openAlertDialog.value) {
                MoreInfoDialog(
                    onDismissRequest = { openAlertDialog.value = false },
                    onConfirmation = { openAlertDialog.value = false },
                    dialogTitle = stringResource(id = R.string.feed_recommendations_why_am_i_seeing_this),
                    dialogText = stringResource(id = R.string.feed_recommendations_reason_you_are_seeing_this),
                )
            }

            PostCardList(
                feed = feed,
                errorToastMessage = errorToastMessage,
                postCardInteractions = postCardInteractions,
                pullToRefreshEnabled = true,
                isFullScreenLoading = true,
                headerContent = {
                    reccs?.let {
                        RecommendationCarousel(reccs = it) { openAlertDialog.value = true }
                    }
                },
                stateKey = LazyListStateKey.FEED
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
            reccs = null,
            postCardInteractions = object : PostCardInteractions {},
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
            reccs = null,
            postCardInteractions = object : PostCardInteractions {},
        )
    }
}