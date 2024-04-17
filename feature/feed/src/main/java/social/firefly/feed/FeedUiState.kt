package social.firefly.feed

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import social.firefly.core.ui.postcard.PostCardUiState

data class FeedUiState(
    val timelineType: TimelineType = TimelineType.FOR_YOU,
    val homeFeed: Flow<PagingData<PostCardUiState>> = emptyFlow(),
    val scrollUpButtonCanShow: Boolean = true
)