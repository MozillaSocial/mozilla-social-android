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

class UnfollowHashTag(
    private val externalScope: CoroutineScope,
    private val showSnackbar: ShowSnackbar,
    private val hashtagRepository: HashtagRepository,
    private val dispatcherIo: CoroutineDispatcher = Dispatchers.IO,
) {

    /**
     * @throws UnfollowFailedException if any error occurred
     */
    @OptIn(PreferUseCase::class)
    suspend operator fun invoke(
        name: String,
    ) = externalScope.async(dispatcherIo) {
        try {
            hashtagRepository.updateFollowing(name, false)
            val hashTag = hashtagRepository.unfollowHashTag(name)
            hashtagRepository.insert(hashTag)
        } catch (e: Exception) {
            hashtagRepository.updateFollowing(name, true)
            showSnackbar(
                text = StringFactory.resource(R.string.error_unfollowing_hashtag),
                isError = true,
            )
            throw UnfollowFailedException(e)
        }
    }.await()

    class UnfollowFailedException(e: Exception) : Exception(e)
}