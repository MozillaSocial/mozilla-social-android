package social.firefly.core.ui.common.hashtag.quickview

import social.firefly.common.utils.StringFactory
import social.firefly.core.ui.common.following.FollowStatus

data class HashTagQuickViewUiState(
    val name: String,
    val details: StringFactory,
    val followStatus: FollowStatus,
)
