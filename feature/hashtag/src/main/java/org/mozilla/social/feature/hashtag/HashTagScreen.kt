package org.mozilla.social.feature.hashtag

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import org.mozilla.social.core.ui.common.MoSoSurface
import org.mozilla.social.core.ui.common.appbar.MoSoCloseableTopAppBar
import org.mozilla.social.core.ui.common.following.FollowingButton
import org.mozilla.social.core.ui.postcard.PostCardInteractions
import org.mozilla.social.core.ui.postcard.PostCardList
import org.mozilla.social.core.ui.postcard.PostCardUiState

@Composable
internal fun HashTagScreen(
    hashTag: String,
    viewModel: HashTagViewModel = koinViewModel(parameters = { parametersOf(hashTag) }),
) {
    HashTagScreen(
        hashTag = hashTag,
        feed = viewModel.feed,
        postCardInteractions = viewModel.postCardDelegate,
        hashTagInteractions = viewModel,
    )

    LaunchedEffect(Unit) {
        viewModel.onScreenViewed()
    }
}

@Composable
private fun HashTagScreen(
    hashTag: String,
    feed: Flow<PagingData<PostCardUiState>>,
    postCardInteractions: PostCardInteractions,
    hashTagInteractions: HashTagInteractions,
) {
    MoSoSurface {
        Column(
            modifier = Modifier.systemBarsPadding(),
        ) {
            MoSoCloseableTopAppBar(
                title = "#$hashTag",
                actions = {
                    FollowingButton(
                        onButtonClicked = { hashTagInteractions.onFollowClicked(hashTag, false) },
                        isFollowing = false
                    )
                }
            )

            PostCardList(
                feed = feed,
                postCardInteractions = postCardInteractions,
                pullToRefreshEnabled = true,
                isFullScreenLoading = true,
            )
        }
    }
}
