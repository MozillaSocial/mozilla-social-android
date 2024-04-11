package social.firefly.core.usecase.mastodon.status

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import social.firefly.common.annotations.PreferUseCase
import social.firefly.common.utils.StringFactory
import social.firefly.core.model.PollVote
import social.firefly.core.navigation.usecases.ShowSnackbar
import social.firefly.core.repository.mastodon.PollRepository
import social.firefly.core.repository.mastodon.StatusRepository
import social.firefly.core.usecase.mastodon.R

class VoteOnPoll(
    private val externalScope: CoroutineScope,
    private val statusRepository: StatusRepository,
    private val pollRepository: PollRepository,
    private val showSnackbar: ShowSnackbar,
    private val dispatcherIo: CoroutineDispatcher = Dispatchers.IO,
) {
    @OptIn(PreferUseCase::class)
    suspend operator fun invoke(
        pollId: String,
        pollChoices: List<Int>,
    ) = externalScope.async(dispatcherIo) {
        try {
            pollRepository.updateOwnVotes(pollId, pollChoices)
            val poll =
                statusRepository.voteOnPoll(
                    pollId,
                    PollVote(pollChoices),
                )
            pollRepository.insert(poll)
        } catch (e: Exception) {
            pollRepository.updateOwnVotes(pollId, null)
            showSnackbar(
                text = StringFactory.resource(R.string.error_voting),
                isError = true,
            )
            throw VoteOnPollFailedException(e)
        }
    }.await()

    class VoteOnPollFailedException(e: Exception) : Exception(e)
}
