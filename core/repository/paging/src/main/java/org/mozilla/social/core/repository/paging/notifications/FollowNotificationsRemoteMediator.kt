package org.mozilla.social.core.repository.paging.notifications

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import kotlinx.coroutines.delay
import org.mozilla.social.common.Rel
import org.mozilla.social.common.getMaxIdValue
import org.mozilla.social.core.database.model.entities.notificationCollections.FollowListNotification
import org.mozilla.social.core.database.model.entities.notificationCollections.FollowListNotificationWrapper
import org.mozilla.social.core.model.Notification
import org.mozilla.social.core.repository.mastodon.DatabaseDelegate
import org.mozilla.social.core.repository.mastodon.NotificationsRepository
import org.mozilla.social.core.usecase.mastodon.notification.SaveNotificationsToDatabase
import timber.log.Timber

@OptIn(ExperimentalPagingApi::class)
class FollowNotificationsRemoteMediator(
    private val notificationsRepository: NotificationsRepository,
    private val databaseDelegate: DatabaseDelegate,
    private val saveNotificationsToDatabase: SaveNotificationsToDatabase,
) : RemoteMediator<Int, FollowListNotificationWrapper>() {
    private var nextKey: String? = null

    @Suppress("ComplexMethod")
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, FollowListNotificationWrapper>
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
                            types = arrayOf(Notification.FollowRequest.VALUE),
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
                            types = arrayOf(Notification.FollowRequest.VALUE),
                        )
                    }
                }

            databaseDelegate.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    notificationsRepository.deleteFollowNotificationsList()
                }

                saveNotificationsToDatabase(response.notifications)
                notificationsRepository.insertFollowNotifications(
                    response.notifications.map {
                        FollowListNotification(
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