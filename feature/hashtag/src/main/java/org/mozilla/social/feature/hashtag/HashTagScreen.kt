package org.mozilla.social.feature.hashtag

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import org.mozilla.social.common.Resource
import org.mozilla.social.core.model.HashTag
import org.mozilla.social.core.ui.common.MoSoSurface
import org.mozilla.social.core.ui.common.appbar.MoSoCloseableTopAppBar
import org.mozilla.social.core.ui.common.error.GenericError
import org.mozilla.social.core.ui.common.following.FollowingButton
import org.mozilla.social.core.ui.common.loading.MaxSizeLoading
import org.mozilla.social.core.ui.postcard.PostCardInteractions
import org.mozilla.social.core.ui.postcard.PostCardList
import org.mozilla.social.core.ui.postcard.PostCardUiState

@Composable
internal fun HashTagScreen(
    hashTag: String,
    viewModel: HashTagViewModel = koinViewModel(parameters = { parametersOf(hashTag) }),
) {

    val uiState: Resource<HashTag> by viewModel.uiState.collectAsStateWithLifecycle()

    HashTagScreen(
        uiState = uiState,
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
    uiState: Resource<HashTag>,
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
                    if (uiState is Resource.Loaded) {
                        FollowingButton(
                            modifier = Modifier
                                .padding(end = 8.dp),
                            onButtonClicked = {
                                hashTagInteractions.onFollowClicked(
                                    hashTag,
                                    uiState.data.following
                                )
                            },
                            isFollowing = uiState.data.following
                        )
                    }
                }
            )

            when (uiState) {
                is Resource.Loading -> {
                    MaxSizeLoading()
                }
                is Resource.Loaded -> {
                    PostCardList(
                        feed = feed,
                        postCardInteractions = postCardInteractions,
                        pullToRefreshEnabled = true,
                        isFullScreenLoading = true,
                    )
                }
                is Resource.Error -> {
                    GenericError(
                        modifier = Modifier.fillMaxSize(),
                        onRetryClicked = { hashTagInteractions.onRetryClicked() }
                    )
                }
            }
        }
    }
}
