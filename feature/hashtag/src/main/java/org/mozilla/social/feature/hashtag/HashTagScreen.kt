package org.mozilla.social.feature.hashtag

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
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
        onCloseClicked = onCloseClicked,
        postCardInteractions = viewModel.postCardDelegate,
    )
}

@Composable
private fun HashTagScreen(
    hashTag: String,
    feed: Flow<PagingData<PostCardUiState>>,
    onCloseClicked: () -> Unit,
    postCardInteractions: PostCardInteractions,
) {
    Column {
        MoSoTopBar(
            title = "#$hashTag",
            onCloseClicked = onCloseClicked,
        )

        PostCardList(
            feed = feed,
            postCardInteractions = postCardInteractions,
        )
    }
}