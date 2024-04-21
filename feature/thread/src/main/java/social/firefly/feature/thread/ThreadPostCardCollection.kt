package social.firefly.feature.thread

import social.firefly.core.ui.postcard.PostCardUiState

data class ThreadPostCardCollection(
    val ancestors: List<PostCardUiState> = emptyList(),
    val mainPost: PostCardUiState? = null,
    val descendants: List<PostCardUiState> = emptyList(),
)