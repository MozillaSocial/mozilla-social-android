package social.firefly.core.ui.hashtagcard.quickview

import social.firefly.common.utils.StringFactory
import social.firefly.core.model.HashTag
import social.firefly.core.ui.common.following.FollowStatus
import social.firefly.core.ui.hashtagcard.R

fun HashTag.toHashTagQuickViewUiState(): HashTagQuickViewUiState {
    val usages = history?.sumOf { it.usageCount } ?: 0
    val days = history?.size ?: 0
    return HashTagQuickViewUiState(
        name = name,
        details = StringFactory.quantityResource(
            resId = R.plurals.hash_tag_details,
            quantity = days,
            usages, days,
        ),
        followStatus = if (following) {
            FollowStatus.FOLLOWING
        } else {
            FollowStatus.NOT_FOLLOWING
        },
    )
}
