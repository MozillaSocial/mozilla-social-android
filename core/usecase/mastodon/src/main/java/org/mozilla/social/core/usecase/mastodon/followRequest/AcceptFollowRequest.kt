package org.mozilla.social.core.usecase.mastodon.followRequest

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import org.mozilla.social.common.annotations.PreferUseCase
import org.mozilla.social.common.utils.StringFactory
import org.mozilla.social.core.model.Notification
import org.mozilla.social.core.navigation.usecases.ShowSnackbar
import org.mozilla.social.core.repository.mastodon.FollowRequestRepository
import org.mozilla.social.core.repository.mastodon.NotificationsRepository
import org.mozilla.social.core.repository.mastodon.RelationshipRepository
import org.mozilla.social.core.usecase.mastodon.R

class AcceptFollowRequest(
    private val externalScope: CoroutineScope,
    private val showSnackbar: ShowSnackbar,
    private val followRequestRepository: FollowRequestRepository,
    private val notificationsRepository: NotificationsRepository,
    private val relationshipRepository: RelationshipRepository,
    private val dispatcherIo: CoroutineDispatcher = Dispatchers.IO,
) {
    /**
     * @throws AcceptRequestFailedException if any error occurred
     */
    @OptIn(PreferUseCase::class)
    suspend operator fun invoke(
        accountId: String,
        notificationId: Int,
    ) = externalScope.async(dispatcherIo) {
        try {
            notificationsRepository.changeNotificationTypeToFollow(notificationId)
            val relationship = followRequestRepository.acceptFollowRequest(accountId)
            relationshipRepository.insert(relationship)
        } catch (e: Exception) {
            notificationsRepository.changeNotificationTypeToFollowRequest(notificationId)
            showSnackbar(
                text = StringFactory.resource(R.string.error_accepting_follow_request),
                isError = true,
            )
            throw AcceptRequestFailedException(e)
        }
    }.await()

    class AcceptRequestFailedException(e: Exception) : Exception(e)
}