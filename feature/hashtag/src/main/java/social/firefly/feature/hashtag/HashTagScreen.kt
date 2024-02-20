package social.firefly.feature.hashtag

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
import androidx.paging.compose.collectAsLazyPagingItems
import kotlinx.coroutines.flow.Flow
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import social.firefly.common.Resource
import social.firefly.core.model.HashTag
import social.firefly.core.ui.common.FfSurface
import social.firefly.core.ui.common.appbar.FfCloseableTopAppBar
import social.firefly.core.ui.common.error.GenericError
import social.firefly.core.ui.common.following.FollowingButton
import social.firefly.core.ui.common.loading.MaxSizeLoading
import social.firefly.core.ui.common.pullrefresh.PullRefreshLazyColumn
import social.firefly.core.ui.postcard.PostCardInteractions
import social.firefly.core.ui.postcard.PostCardUiState
import social.firefly.core.ui.postcard.postListContent

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
    FfSurface {
        Column(
            modifier = Modifier.systemBarsPadding(),
        ) {
            FfCloseableTopAppBar(
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
                    val feedListState = feed.collectAsLazyPagingItems()

                    PullRefreshLazyColumn(
                        lazyPagingItems = feedListState,
                    ) {
                        postListContent(
                            lazyPagingItems = feedListState,
                            postCardInteractions = postCardInteractions,
                        )
                    }
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
