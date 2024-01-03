package org.mozilla.social.feature.hashtag

interface HashTagInteractions {
    fun onScreenViewed() = Unit
    fun onFollowClicked(name: String, isFollowing: Boolean) = Unit
}
