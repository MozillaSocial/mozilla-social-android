package social.firefly.feature.discover

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import social.firefly.common.utils.StringFactory
import social.firefly.core.ui.hashtagcard.quickview.HashTagQuickViewUiState
import social.firefly.core.ui.postcard.PostCardUiState

sealed class DiscoverTab(
    val tabTitle: StringFactory,
) {
    data class Hashtags(
        val pagingDataFlow: Flow<PagingData<HashTagQuickViewUiState>>
    ) : DiscoverTab(
        tabTitle = StringFactory.resource(R.string.hashtags_tab),
    )

    data class Posts(
        val pagingDataFlow: Flow<PagingData<PostCardUiState>>
    ) : DiscoverTab(
        tabTitle = StringFactory.resource(R.string.posts_tab),
    )
}