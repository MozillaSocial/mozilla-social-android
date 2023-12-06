package org.mozilla.social.feature.favorites

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import org.koin.androidx.compose.koinViewModel
import org.mozilla.social.core.ui.common.MoSoSurface
import org.mozilla.social.core.ui.common.appbar.MoSoCloseableTopAppBar
import org.mozilla.social.core.ui.postcard.PostCardInteractions
import org.mozilla.social.core.ui.postcard.PostCardList
import org.mozilla.social.core.ui.postcard.PostCardUiState

@Composable
internal fun FavoritesScreen(
    viewModel: FavoritesViewModel = koinViewModel()
) {
    FavoritesScreen(
        feed = viewModel.feed,
        postCardInteractions = viewModel.postCardDelegate
    )
}

@Composable
private fun FavoritesScreen(
    feed: Flow<PagingData<PostCardUiState>>,
    postCardInteractions: PostCardInteractions,
) {
    MoSoSurface {
        Column(Modifier.systemBarsPadding()) {
            MoSoCloseableTopAppBar(title = stringResource(id = R.string.favorites_title))

            PostCardList(
                feed = feed,
                postCardInteractions = postCardInteractions,
                pullToRefreshEnabled = true,
                isFullScreenLoading = true,
            )
        }
    }
}