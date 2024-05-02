package social.firefly.core.repository.mastodon

import social.firefly.core.database.dao.TrendingStatusDao
import social.firefly.core.database.model.entities.statusCollections.DbTrendingStatus
import social.firefly.core.model.Status
import social.firefly.core.network.mastodon.TrendsApi
import social.firefly.core.model.PageItem
import social.firefly.core.repository.mastodon.model.status.toExternalModel

class TrendingStatusRepository(
    private val api: TrendsApi,
    private val dao: TrendingStatusDao,
) {

    fun pagingSource() = dao.pagingSource()

    suspend fun getRemotely(
        limit: Int,
        offset: Int
    ): List<Status> = api.getTrendingStatuses(
        limit = limit,
        offset = offset
    ).map { it.toExternalModel() }

    suspend fun saveLocally(
        currentPage: List<PageItem<Status>>,
    ) = dao.upsertAll(
        currentPage.map { status ->
            DbTrendingStatus(
                statusId = status.item.statusId,
                position = status.position,
            )
        }
    )

    suspend fun deleteAll() {
        dao.deleteAll()
    }
}
