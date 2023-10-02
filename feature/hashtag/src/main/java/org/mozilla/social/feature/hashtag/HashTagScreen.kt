package org.mozilla.social.feature.hashtag

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import org.mozilla.social.common.utils.StringFactory
import org.mozilla.social.core.designsystem.component.MoSoTopBar
import org.mozilla.social.core.ui.postcard.PostCardInteractions
import org.mozilla.social.core.ui.postcard.PostCardList
import org.mozilla.social.core.ui.postcard.PostCardNavigation
import org.mozilla.social.core.ui.postcard.PostCardUiState

@Composable
internal fun HashTagRoute(
    hashTag: String,
    onCloseClicked: () -> Unit,
    postCardNavigation: PostCardNavigation,
    viewModel: HashTagViewModel = koinViewModel(parameters = { parametersOf(
        hashTag,
        postCardNavigation,
    ) } )
) {
    HashTagScreen(
        hashTag = hashTag,
        feed = viewModel.feed,
        errorToastMessage = viewModel.postCardDelegate.errorToastMessage,
        onCloseClicked = onCloseClicked,
        postCardInteractions = viewModel.postCardDelegate,
    )
}

@Composable
private fun HashTagScreen(
    hashTag: String,
    feed: Flow<PagingData<PostCardUiState>>,
    errorToastMessage: SharedFlow<StringFactory>,
    onCloseClicked: () -> Unit,
    postCardInteractions: PostCardInteractions,
) {
    Column {
        MoSoTopBar(
            title = "#$hashTag",
            onIconClicked = onCloseClicked,
        )

        PostCardList(
            feed = feed,
            errorToastMessage = errorToastMessage,
            postCardInteractions = postCardInteractions,
            enablePullToRefresh = true,
            fullScreenRefreshStates = true,
        )
    }
}