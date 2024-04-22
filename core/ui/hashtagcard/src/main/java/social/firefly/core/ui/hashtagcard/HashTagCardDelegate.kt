package social.firefly.core.ui.hashtagcard

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import social.firefly.core.navigation.NavigationDestination
import social.firefly.core.navigation.usecases.NavigateTo
import social.firefly.core.ui.common.following.FollowStatus
import social.firefly.core.usecase.mastodon.hashtag.FollowHashTag
import social.firefly.core.usecase.mastodon.hashtag.UnfollowHashTag
import timber.log.Timber

class HashTagCardDelegate(
    private val navigateTo: NavigateTo,
    private val coroutineScope: CoroutineScope,
    private val followHashTag: FollowHashTag,
    private val unfollowHashTag: UnfollowHashTag,
) : HashTagInteractions {

    override fun onHashtagClick(name: String) {
        navigateTo(NavigationDestination.HashTag(name))
    }

    override fun onHashTagFollowClicked(name: String, followStatus: FollowStatus) {
        coroutineScope.launch {
            when (followStatus) {
                FollowStatus.FOLLOWING,
                FollowStatus.PENDING_REQUEST -> {
                    try {
                        unfollowHashTag(name)
                    } catch (e: UnfollowHashTag.UnfollowFailedException) {
                        Timber.e(e)
                    }
                }
                FollowStatus.NOT_FOLLOWING -> {
                    try {
                        followHashTag(name)
                    } catch (e: FollowHashTag.FollowFailedException) {
                        Timber.e(e)
                    }
                }
            }
        }
    }
}