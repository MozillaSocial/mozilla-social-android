package org.mozilla.social.core.usecase.mastodon.hashtag

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import org.mozilla.social.common.annotations.PreferUseCase
import org.mozilla.social.common.utils.StringFactory
import org.mozilla.social.core.navigation.usecases.ShowSnackbar
import org.mozilla.social.core.repository.mastodon.HashtagRepository
import org.mozilla.social.core.usecase.mastodon.R

class FollowHashTag(
    private val externalScope: CoroutineScope,
    private val showSnackbar: ShowSnackbar,
    private val hashtagRepository: HashtagRepository,
    private val dispatcherIo: CoroutineDispatcher = Dispatchers.IO,
) {

    /**
     * @throws FollowFailedException if any error occurred
     */
    @OptIn(PreferUseCase::class)
    suspend operator fun invoke(
        name: String,
    ) = externalScope.async(dispatcherIo) {
        try {
            hashtagRepository.updateFollowing(name, true)
            val hashTag = hashtagRepository.followHashTag(name)
            hashtagRepository.insert(hashTag)
        } catch (e: Exception) {
            hashtagRepository.updateFollowing(name, false)
            showSnackbar(
                text = StringFactory.resource(R.string.error_following_hashtag),
                isError = true,
            )
            throw FollowFailedException(e)
        }
    }.await()

    class FollowFailedException(e: Exception) : Exception(e)
}