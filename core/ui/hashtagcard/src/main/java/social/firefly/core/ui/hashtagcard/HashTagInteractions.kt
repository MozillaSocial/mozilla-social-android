package social.firefly.core.ui.hashtagcard

import social.firefly.core.ui.common.following.FollowStatus

interface HashTagInteractions {
    fun onHashTagFollowClicked(name: String, followStatus: FollowStatus)
    fun onHashtagClick(name: String)
}

object HashTagInteractionsNoOp : HashTagInteractions {
    override fun onHashTagFollowClicked(name: String, followStatus: FollowStatus) = Unit
    override fun onHashtagClick(name: String) = Unit
}