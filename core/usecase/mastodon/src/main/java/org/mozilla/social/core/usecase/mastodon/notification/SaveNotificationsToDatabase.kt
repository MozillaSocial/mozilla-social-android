package org.mozilla.social.core.usecase.mastodon.notification

import org.mozilla.social.common.annotations.PreferUseCase
import org.mozilla.social.core.model.Notification
import org.mozilla.social.core.model.Relationship
import org.mozilla.social.core.repository.mastodon.AccountRepository
import org.mozilla.social.core.repository.mastodon.DatabaseDelegate
import org.mozilla.social.core.repository.mastodon.NotificationsRepository
import org.mozilla.social.core.repository.mastodon.RelationshipRepository
import org.mozilla.social.core.usecase.mastodon.status.SaveStatusToDatabase

class SaveNotificationsToDatabase(
    private val notificationsRepository: NotificationsRepository,
    private val databaseDelegate: DatabaseDelegate,
    private val accountRepository: AccountRepository,
    private val saveStatusToDatabase: SaveStatusToDatabase,
    private val relationshipRepository: RelationshipRepository,
) {

    @OptIn(PreferUseCase::class)
    suspend operator fun invoke(
        notifications: List<Notification>,
        relationships: List<Relationship>,
    ) {
        databaseDelegate.withTransaction {
            accountRepository.insertAll(notifications.map { it.account })
            relationshipRepository.insertAll(relationships)
            notifications.forEach {
                when (it) {
                    is Notification.StatusUpdated -> saveStatusToDatabase(it.status)
                    is Notification.PollEnded -> saveStatusToDatabase(it.status)
                    is Notification.Favorite -> saveStatusToDatabase(it.status)
                    is Notification.Repost -> saveStatusToDatabase(it.status)
                    is Notification.NewStatus -> saveStatusToDatabase(it.status)
                    is Notification.Mention -> saveStatusToDatabase(it.status)
                    else -> {}
                }
            }
            notificationsRepository.insertAll(notifications)
        }
    }
}