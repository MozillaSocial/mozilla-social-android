package social.firefly.core.ui.hashtagcard

import social.firefly.core.ui.common.following.FollowStatus

interface HashtagInteractions {
    fun onHashTagFollowClicked(name: String, followStatus: FollowStatus)
    fun onHashtagClick(name: String)
}