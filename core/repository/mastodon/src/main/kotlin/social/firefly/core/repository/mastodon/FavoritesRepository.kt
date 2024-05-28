package social.firefly.core.repository.mastodon

import androidx.paging.PagingSource
import social.firefly.core.database.dao.FavoritesTimelineStatusDao
import social.firefly.core.database.model.entities.statusCollections.FavoritesTimelineStatus
import social.firefly.core.database.model.entities.statusCollections.FavoritesTimelineStatusWrapper
import social.firefly.core.model.Status
import social.firefly.core.model.paging.MastodonPagedResponse
import social.firefly.core.network.mastodon.FavoritesApi
import social.firefly.core.network.mastodon.utils.toMastodonPagedResponse
import social.firefly.core.repository.mastodon.model.status.toExternalModel

class FavoritesRepository(
    private val api: FavoritesApi,
    private val dao: FavoritesTimelineStatusDao,
) {
    suspend fun getFavorites(
        maxId: String? = null,
        sinceId: String? = null,
        minId: String? = null,
        limit: Int? = null,
    ): MastodonPagedResponse<Status> = api.getFavorites(
        maxId = maxId,
        sinceId = sinceId,
        minId = minId,
        limit = limit,
    ).toMastodonPagedResponse { it.toExternalModel() }

    fun getPagingSource(): PagingSource<Int, FavoritesTimelineStatusWrapper> =
        dao.favoritesTimelinePagingSource()

    suspend fun deleteFavoritesTimeline() {
        dao.deleteFavoritesTimelines()
    }

    suspend fun insertAll(
        statuses: List<FavoritesTimelineStatus>
    ) = dao.upsertAll(statuses)

    suspend fun insert(
        status: FavoritesTimelineStatus
    ) = dao.upsert(status)

    suspend fun deleteStatusFromTimeline(
        statusId: String,
    ) = dao.deletePost(statusId)

    suspend fun getStatusFromTimeline(
        statusId: String,
    ): FavoritesTimelineStatus = dao.getStatus(statusId)
}