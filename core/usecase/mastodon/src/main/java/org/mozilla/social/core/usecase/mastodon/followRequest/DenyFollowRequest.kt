package org.mozilla.social.core.usecase.mastodon.followRequest

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import org.mozilla.social.common.annotations.PreferUseCase
import org.mozilla.social.common.utils.StringFactory
import org.mozilla.social.core.database.model.entities.notificationCollections.MainNotification
import org.mozilla.social.core.model.Notification
import org.mozilla.social.core.navigation.usecases.ShowSnackbar
import org.mozilla.social.core.repository.mastodon.FollowRequestRepository
import org.mozilla.social.core.repository.mastodon.NotificationsRepository
import org.mozilla.social.core.repository.mastodon.RelationshipRepository
import org.mozilla.social.core.usecase.mastodon.R
import org.mozilla.social.core.usecase.mastodon.notification.SaveNotificationsToDatabase

class DenyFollowRequest(
    private val externalScope: CoroutineScope,
    private val showSnackbar: ShowSnackbar,
    private val followRequestRepository: FollowRequestRepository,
    private val notificationsRepository: NotificationsRepository,
    private val relationshipRepository: RelationshipRepository,
    private val saveNotificationsToDatabase: SaveNotificationsToDatabase,
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
                saveNotificationsToDatabase(notification)
                notificationsRepository.insertAllMainNotifications(
                    listOf(
                        MainNotification(notificationId)
                    )
                )
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