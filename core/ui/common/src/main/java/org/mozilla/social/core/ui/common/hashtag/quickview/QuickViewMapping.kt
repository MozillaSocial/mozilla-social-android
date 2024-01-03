package org.mozilla.social.core.ui.common.hashtag.quickview

import org.mozilla.social.common.utils.StringFactory
import org.mozilla.social.core.model.HashTag
import org.mozilla.social.core.ui.common.R

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
        isFollowing = following,
    )
}
