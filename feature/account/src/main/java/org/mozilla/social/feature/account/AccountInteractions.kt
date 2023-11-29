package org.mozilla.social.feature.account

import org.mozilla.social.core.model.AccountTimelineType

interface AccountInteractions : OverflowInteractions {
    fun onFollowersClicked() = Unit

    fun onFollowingClicked() = Unit

    fun onFollowClicked() = Unit

    fun onUnfollowClicked() = Unit

    fun onRetryClicked() = Unit

    fun onTabClicked(timelineType: AccountTimelineType) = Unit

    fun onSettingsClicked() = Unit

    fun onScreenViewed() = Unit

    fun onEditAccountClicked() = Unit
}
