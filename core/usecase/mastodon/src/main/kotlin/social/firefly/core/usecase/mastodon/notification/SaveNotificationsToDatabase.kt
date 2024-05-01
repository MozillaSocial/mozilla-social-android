package social.firefly.core.usecase.mastodon.notification

import social.firefly.common.annotations.PreferUseCase
import social.firefly.core.model.Notification
import social.firefly.core.repository.mastodon.AccountRepository
import social.firefly.core.repository.mastodon.DatabaseDelegate
import social.firefly.core.repository.mastodon.NotificationsRepository
import social.firefly.core.usecase.mastodon.status.SaveStatusToDatabase

class SaveNotificationsToDatabase(
    private val notificationsRepository: NotificationsRepository,
    private val databaseDelegate: DatabaseDelegate,
    private val accountRepository: AccountRepository,
    private val saveStatusToDatabase: SaveStatusToDatabase,
) {

    @OptIn(PreferUseCase::class)
    suspend operator fun invoke(notifications: List<Notification>) {
        databaseDelegate.withTransaction {
            accountRepository.insertAll(notifications.map { it.account })
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

    suspend operator fun invoke(vararg notifications: Notification) {
        invoke(notifications.toList())
    }
}