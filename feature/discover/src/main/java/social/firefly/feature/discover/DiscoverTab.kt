package social.firefly.feature.discover

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import social.firefly.common.utils.StringFactory
import social.firefly.core.ui.common.hashtag.quickview.HashTagQuickViewUiState
import social.firefly.core.ui.postcard.PostCardUiState

sealed class DiscoverTab(val tabTitle: StringFactory, val index: Int) {
    data class Hashtags(val pagingDataFlow: Flow<PagingData<HashTagQuickViewUiState>>) :
        DiscoverTab(StringFactory.resource(R.string.hashtags_tab), 0)

    data class Posts(val pagingDataFlow: Flow<PagingData<PostCardUiState>>) :
        DiscoverTab(StringFactory.resource(R.string.posts_tab), 1)
}