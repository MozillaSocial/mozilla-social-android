package org.mozilla.social.core.ui.common.hashtag.quickview

import org.mozilla.social.common.utils.StringFactory

data class HashTagQuickViewUiState(
    val name: String,
    val details: StringFactory,
    val isFollowing: Boolean,
)
