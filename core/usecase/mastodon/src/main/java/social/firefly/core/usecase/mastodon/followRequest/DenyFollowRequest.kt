package social.firefly.core.usecase.mastodon.followRequest

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import social.firefly.common.annotations.PreferUseCase
import social.firefly.common.utils.StringFactory
import social.firefly.core.database.model.entities.notificationCollections.MainNotification
import social.firefly.core.model.Notification
import social.firefly.core.navigation.usecases.ShowSnackbar
import social.firefly.core.repository.mastodon.DatabaseDelegate
import social.firefly.core.repository.mastodon.FollowRequestRepository
import social.firefly.core.repository.mastodon.NotificationsRepository
import social.firefly.core.repository.mastodon.RelationshipRepository
import social.firefly.core.usecase.mastodon.R
import social.firefly.core.usecase.mastodon.notification.SaveNotificationsToDatabase

class DenyFollowRequest(
    private val externalScope: CoroutineScope,
    private val showSnackbar: ShowSnackbar,
    private val followRequestRepository: FollowRequestRepository,
    private val notificationsRepository: NotificationsRepository,
    private val relationshipRepository: RelationshipRepository,
    private val saveNotificationsToDatabase: SaveNotificationsToDatabase,
    private val databaseDelegate: DatabaseDelegate,
    private val dispatcherIo: CoroutineDispatcher = Dispatchers.IO,
) {
    /**
     * @throws DenyRequestFailedException if any error occurred
     */
    @OptIn(PreferUseCase::class)
    suspend operator fun invoke(
        accountId: String,
        notificationId: Int,
    ) = externalScope.async(dispatcherIo) {
        var notification: Notification? = null
        try {
            notification = notificationsRepository.getNotification(notificationId)
            notificationsRepository.deleteNotification(notificationId)
            val relationship = followRequestRepository.rejectFollowRequest(accountId)
            relationshipRepository.insert(relationship)
        } catch (e: Exception) {
            notification?.let {
                databaseDelegate.withTransaction {
                    saveNotificationsToDatabase(notification)
                    notificationsRepository.insertAllMainNotifications(
                        listOf(MainNotification(notificationId))
                    )
                }
            }
            showSnackbar(
                text = StringFactory.resource(R.string.error_rejcting_follow_request),
                isError = true,
            )
            throw DenyRequestFailedException(e)
        }
    }.await()

    class DenyRequestFailedException(e: Exception) : Exception(e)
}