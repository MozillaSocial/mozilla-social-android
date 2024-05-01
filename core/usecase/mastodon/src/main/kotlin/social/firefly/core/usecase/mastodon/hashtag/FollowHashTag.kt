package social.firefly.core.usecase.mastodon.hashtag

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import social.firefly.common.annotations.PreferUseCase
import social.firefly.common.utils.StringFactory
import social.firefly.core.navigation.usecases.ShowSnackbar
import social.firefly.core.repository.mastodon.HashtagRepository
import social.firefly.core.usecase.mastodon.R

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