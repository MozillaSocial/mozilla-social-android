package social.firefly.feature.account

interface OverflowInteractions {
    fun onOverflowFavoritesClicked() = Unit
    fun onOverflowShareClicked() = Unit
    fun onOverflowMuteClicked() = Unit
    fun onOverflowUnmuteClicked() = Unit
    fun onOverflowBlockClicked() = Unit
    fun onOverflowUnblockClicked() = Unit
    fun onOverflowReportClicked() = Unit
    fun onOverflowFollowedHashTagsClicked() = Unit
    fun onOverflowBookmarksClicked() = Unit
    fun onOverflowBlockDomainClicked() = Unit
    fun onOverflowUnblockDomainClicked() = Unit
}
