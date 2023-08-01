package org.mozilla.social.core.ui.postcard

interface PostCardInteractions {
    fun onReplyClicked(statusId: String, ) = Unit
    fun onBoostClicked() = Unit
    fun onFavoriteClicked() = Unit
    fun onShareClicked() = Unit
}