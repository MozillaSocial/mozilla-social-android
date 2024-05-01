package social.firefly.core.usecase.mastodon.followRequest

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import social.firefly.common.annotations.PreferUseCase
import social.firefly.common.utils.StringFactory
import social.firefly.core.navigation.usecases.ShowSnackbar
import social.firefly.core.repository.mastodon.FollowRequestRepository
import social.firefly.core.repository.mastodon.NotificationsRepository
import social.firefly.core.repository.mastodon.RelationshipRepository
import social.firefly.core.usecase.mastodon.R

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