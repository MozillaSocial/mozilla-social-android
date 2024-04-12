@file:OptIn(ExperimentalPagingApi::class)

package social.firefly.core.repository.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingSource
import social.firefly.core.database.model.entities.statusCollections.TrendingStatusWrapper
import social.firefly.core.model.Status
import social.firefly.core.repository.common.FFLocalSource
import social.firefly.core.repository.common.FFRemoteSource
import social.firefly.core.repository.common.PageItem
import social.firefly.core.repository.mastodon.DatabaseDelegate
import social.firefly.core.repository.mastodon.TrendingStatusRepository
import social.firefly.core.repository.mastodon.model.status.toExternalModel
import social.firefly.core.repository.paging.common.FfPager
import social.firefly.core.usecase.mastodon.status.SaveStatusToDatabase

class TrendingStatusPager(
    private val databaseDelegate: DatabaseDelegate,
    private val saveStatusToDatabase: SaveStatusToDatabase,
    private val trendingStatusRepository: TrendingStatusRepository,
): FfPager<Status, TrendingStatusWrapper> {

    override fun map(dbo: TrendingStatusWrapper): Status {
        return dbo.status.toExternalModel()
    }

    override suspend fun getRemotely(limit: Int, offset: Int): List<Status> {
        return trendingStatusRepository.getRemotely(limit, offset)
    }

    override suspend fun saveLocally(currentPage: List<PageItem<Status>>) {
        databaseDelegate.withTransaction {
            saveStatusToDatabase(currentPage.map { it.item })
            trendingStatusRepository.saveLocally(currentPage)
        }
    }

    override fun pagingSource(): PagingSource<Int, TrendingStatusWrapper> =
        trendingStatusRepository.pagingSource()
}
