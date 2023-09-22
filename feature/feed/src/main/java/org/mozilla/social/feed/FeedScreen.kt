package org.mozilla.social.feed

import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.tooling.preview.Preview
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
import org.mozilla.social.core.ui.postcard.PostCardInteractions
import org.mozilla.social.core.ui.postcard.PostCardList
import org.mozilla.social.core.ui.postcard.PostCardNavigation
import org.mozilla.social.core.ui.postcard.PostCardUiState
import org.mozilla.social.model.Recommendation

@Composable
internal fun FeedRoute(
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
        PostCardList(
            feed = feed,
            errorToastMessage = errorToastMessage,
            postCardInteractions = postCardInteractions,
            reccs = reccs,
        )
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