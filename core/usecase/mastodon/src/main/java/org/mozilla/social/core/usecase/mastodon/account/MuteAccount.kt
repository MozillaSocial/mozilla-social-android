package org.mozilla.social.core.usecase.mastodon.account

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import org.mozilla.social.common.annotations.PreferUseCase
import org.mozilla.social.common.utils.StringFactory
import org.mozilla.social.core.database.SocialDatabase
import org.mozilla.social.core.navigation.usecases.ShowSnackbar
import org.mozilla.social.core.repository.mastodon.AccountRepository
import org.mozilla.social.core.repository.mastodon.RelationshipRepository
import org.mozilla.social.core.repository.mastodon.TimelineRepository
import org.mozilla.social.core.usecase.mastodon.R

class MuteAccount(
    private val externalScope: CoroutineScope,
    private val showSnackbar: ShowSnackbar,
    private val accountRepository: AccountRepository,
    private val timelineRepository: TimelineRepository,
    private val relationshipRepository: RelationshipRepository,
    private val socialDatabase: SocialDatabase,
    private val dispatcherIo: CoroutineDispatcher = Dispatchers.IO,
) {
    /**
     * @throws MuteFailedException if any error occurred
     */
    @OptIn(PreferUseCase::class)
    suspend operator fun invoke(accountId: String) =
        externalScope.async(dispatcherIo) {
            try {
                socialDatabase.homeTimelineDao().removePostsFromAccount(accountId)
                socialDatabase.localTimelineDao().removePostsFromAccount(accountId)
                timelineRepository.removePostsFromFederatedTimelineForAccount(accountId)
                socialDatabase.relationshipsDao().updateMuted(accountId, true)
                val relationship = accountRepository.muteAccount(accountId)
                relationshipRepository.insert(relationship)
            } catch (e: Exception) {
                socialDatabase.relationshipsDao().updateMuted(accountId, false)
                showSnackbar(
                    text = StringFactory.resource(R.string.error_muting_account),
                    isError = true,
                )
                throw MuteFailedException(e)
            }
        }.await()

    class MuteFailedException(e: Exception) : Exception(e)
}
