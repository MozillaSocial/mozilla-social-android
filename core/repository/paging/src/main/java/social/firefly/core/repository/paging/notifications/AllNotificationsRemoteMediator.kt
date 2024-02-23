package social.firefly.core.repository.paging.notifications

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import kotlinx.coroutines.delay
import social.firefly.common.Rel
import social.firefly.common.getMaxIdValue
import social.firefly.core.database.model.entities.notificationCollections.MainNotification
import social.firefly.core.database.model.entities.notificationCollections.MainNotificationWrapper
import social.firefly.core.repository.mastodon.DatabaseDelegate
import social.firefly.core.repository.mastodon.NotificationsRepository
import social.firefly.core.usecase.mastodon.notification.SaveNotificationsToDatabase
import timber.log.Timber

@OptIn(ExperimentalPagingApi::class)
class AllNotificationsRemoteMediator(
    private val notificationsRepository: NotificationsRepository,
    private val databaseDelegate: DatabaseDelegate,
    private val saveNotificationsToDatabase: SaveNotificationsToDatabase,
) : RemoteMediator<Int, MainNotificationWrapper>() {
    private var nextKey: String? = null

    @Suppress("ComplexMethod")
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, MainNotificationWrapper>
    ): MediatorResult {
        return try {
            var pageSize: Int = state.config.pageSize
            val response =
                when (loadType) {
                    LoadType.REFRESH -> {
                        pageSize = state.config.initialLoadSize
                        notificationsRepository.getNotifications(
                            maxId = null,
                            limit = pageSize,
                            excludeTypes = arrayOf(
                                "admin.sign_up",
                                "admin.report",
                                "severed_relationships",
                            ),
                        )
                    }

                    LoadType.PREPEND -> {
                        return MediatorResult.Success(endOfPaginationReached = true)
                    }

                    LoadType.APPEND -> {
                        if (nextKey == null) {
                            return MediatorResult.Success(endOfPaginationReached = true)
                        }
                        notificationsRepository.getNotifications(
                            maxId = nextKey,
                            limit = pageSize,
                            excludeTypes = arrayOf(
                                "admin.sign_up",
                                "admin.report",
                                "severed_relationships",
                            ),
                        )
                    }
                }

            databaseDelegate.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    notificationsRepository.deleteMainNotificationsList()
                }

                saveNotificationsToDatabase(response.notifications)
                notificationsRepository.insertAllMainNotifications(
                    response.notifications.map {
                        MainNotification(
                            id = it.id,
                        )
                    }
                )
            }

            nextKey = response.pagingLinks?.getMaxIdValue()

            // There seems to be some race condition for refreshes.  Subsequent pages do
            // not get loaded because once we return a mediator result, the next append
            // and prepend happen right away.  The paging source doesn't have enough time
            // to collect the initial page from the database, so the [state] we get as
            // a parameter in this load method doesn't have any data in the pages, so
            // it's assumed we've reached the end of pagination, and nothing gets loaded
            // ever again.
            if (loadType == LoadType.REFRESH) {
                delay(REFRESH_DELAY)
            }

            MediatorResult.Success(
                endOfPaginationReached = response.pagingLinks?.find { it.rel == Rel.NEXT } == null,
            )
        } catch (e: Exception) {
            Timber.e(e)
            MediatorResult.Error(e)
        }
    }

    companion object {
        private const val REFRESH_DELAY = 200L
    }
}