package social.firefly.core.repository.paging.pagers.status

import androidx.paging.PagingSource
import social.firefly.core.database.model.entities.statusCollections.FavoritesTimelineStatus
import social.firefly.core.database.model.entities.statusCollections.FavoritesTimelineStatusWrapper
import social.firefly.core.model.PageItem
import social.firefly.core.model.Status
import social.firefly.core.model.paging.MastodonPagedResponse
import social.firefly.core.repository.mastodon.DatabaseDelegate
import social.firefly.core.repository.mastodon.FavoritesRepository
import social.firefly.core.repository.mastodon.model.status.toExternalModel
import social.firefly.core.repository.paging.IdBasedPager
import social.firefly.core.usecase.mastodon.status.GetInReplyToAccountNames
import social.firefly.core.usecase.mastodon.status.SaveStatusToDatabase

class FavoritesPager(
    private val favoritesRepository: FavoritesRepository,
    private val saveStatusToDatabase: SaveStatusToDatabase,
    private val databaseDelegate: DatabaseDelegate,
    private val getInReplyToAccountNames: GetInReplyToAccountNames,
) : IdBasedPager<Status, FavoritesTimelineStatusWrapper> {
    override fun mapDbObjectToExternalModel(dbo: FavoritesTimelineStatusWrapper): Status =
        dbo.status.toExternalModel()

    override suspend fun saveLocally(items: List<PageItem<Status>>, isRefresh: Boolean) {
        databaseDelegate.withTransaction {
            if (isRefresh) favoritesRepository.deleteFavoritesTimeline()
            saveStatusToDatabase(items.map { it.item })
            favoritesRepository.insertAll(
                items.map { item ->
                    FavoritesTimelineStatus(
                        statusId = item.item.statusId,
                        position = item.position,
                    )
                }
            )
        }
    }

    override suspend fun getRemotely(limit: Int, nextKey: String?): MastodonPagedResponse<Status> {
        val response = favoritesRepository.getFavorites(
            maxId = nextKey,
            limit = limit,
        )

        return getInReplyToAccountNames(response)
    }

    override fun pagingSource(): PagingSource<Int, FavoritesTimelineStatusWrapper> =
        favoritesRepository.getPagingSource()
}