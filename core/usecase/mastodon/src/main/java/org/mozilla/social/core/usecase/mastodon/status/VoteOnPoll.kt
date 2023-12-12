package org.mozilla.social.core.usecase.mastodon.status

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import org.mozilla.social.common.annotations.PreferUseCase
import org.mozilla.social.common.utils.StringFactory
import org.mozilla.social.core.model.PollVote
import org.mozilla.social.core.navigation.usecases.ShowSnackbar
import org.mozilla.social.core.repository.mastodon.PollRepository
import org.mozilla.social.core.repository.mastodon.StatusRepository
import org.mozilla.social.core.usecase.mastodon.R

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
