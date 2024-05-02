@file:OptIn(ExperimentalPagingApi::class)

package social.firefly.core.repository.paging.pagers

import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingSource
import social.firefly.core.database.model.entities.statusCollections.TrendingStatusWrapper
import social.firefly.core.model.Status
import social.firefly.core.model.PageItem
import social.firefly.core.repository.mastodon.DatabaseDelegate
import social.firefly.core.repository.mastodon.TrendingStatusRepository
import social.firefly.core.repository.mastodon.model.status.toExternalModel
import social.firefly.core.repository.paging.IndexBasedPager
import social.firefly.core.usecase.mastodon.status.SaveStatusToDatabase

class TrendingStatusPager(
    private val databaseDelegate: DatabaseDelegate,
    private val saveStatusToDatabase: SaveStatusToDatabase,
    private val trendingStatusRepository: TrendingStatusRepository,
): IndexBasedPager<Status, TrendingStatusWrapper> {

    override fun mapDbObjectToExternalModel(dbo: TrendingStatusWrapper): Status =
        dbo.status.toExternalModel()

    override fun pagingSource(): PagingSource<Int, TrendingStatusWrapper> =
        trendingStatusRepository.pagingSource()

    override suspend fun saveLocally(items: List<PageItem<Status>>, isRefresh: Boolean) {
        databaseDelegate.withTransaction {
            if (isRefresh) trendingStatusRepository.deleteAll()
            saveStatusToDatabase(items.map { it.item })
            trendingStatusRepository.saveLocally(items)
        }
    }

    override suspend fun getRemotely(limit: Int, offset: Int): List<Status> =
        trendingStatusRepository.getRemotely(limit, offset)
}
