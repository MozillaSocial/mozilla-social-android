package social.firefly.feature.thread

import social.firefly.core.ui.postcard.DepthLinesUiState
import social.firefly.core.ui.postcard.PostCardUiState

data class ThreadPostCardCollection(
    val ancestors: List<PostCardUiState> = emptyList(),
    val mainPost: PostCardUiState? = null,
    val descendants: List<ThreadDescendant> = emptyList(),
)

sealed class ThreadDescendant(
    val id: String
) {
    data class PostCard(
        val uiState: PostCardUiState
    ) : ThreadDescendant(uiState.statusId)

    data class ViewMore(
        val depthLinesUiState: DepthLinesUiState,
        val count: Int,
        val statusId: String,
    ) : ThreadDescendant("$statusId - view more")
}