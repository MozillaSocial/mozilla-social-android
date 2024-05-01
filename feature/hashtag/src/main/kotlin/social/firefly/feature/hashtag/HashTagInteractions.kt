package social.firefly.feature.hashtag

interface HashTagInteractions {
    fun onScreenViewed() = Unit
    fun onFollowClicked(name: String, isFollowing: Boolean) = Unit
    fun onRetryClicked() = Unit
}
