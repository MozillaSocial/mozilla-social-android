package social.firefly.core.ui.common.hashtag.quickview

import social.firefly.common.utils.StringFactory

data class HashTagQuickViewUiState(
    val name: String,
    val details: StringFactory,
    val isFollowing: Boolean,
)
