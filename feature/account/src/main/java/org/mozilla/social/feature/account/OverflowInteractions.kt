package org.mozilla.social.feature.account

interface OverflowInteractions {
    fun onOverflowShareClicked() = Unit

    fun onOverflowMuteClicked() = Unit

    fun onOverflowUnmuteClicked() = Unit

    fun onOverflowBlockClicked() = Unit

    fun onOverflowUnblockClicked() = Unit

    fun onOverflowReportClicked() = Unit
}
