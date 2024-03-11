@file:OptIn(ExperimentalPagingApi::class)

package social.firefly.core.repository.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import social.firefly.core.database.model.entities.statusCollections.TrendingStatusWrapper
import social.firefly.core.model.Status
import social.firefly.core.repository.mastodon.DatabaseDelegate
import social.firefly.core.repository.mastodon.FFLocalSource
import social.firefly.core.repository.mastodon.FFRemoteSource
import social.firefly.core.repository.mastodon.PageItem
import social.firefly.core.repository.mastodon.TrendingStatusRepository
import social.firefly.core.repository.mastodon.model.status.toExternalModel
import social.firefly.core.usecase.mastodon.status.SaveStatusToDatabase

class TrendingStatusRemoteMediator(
    override val localSource: TrendingStatusLocalSource,
    override val remoteSource: TrendingStatusRemoteSource,
) : FfRemoteMediator<Status, TrendingStatusWrapper>()

class TrendingStatusLocalSource(
    private val databaseDelegate: DatabaseDelegate,
    private val saveStatusToDatabase: SaveStatusToDatabase,
    private val trendingStatusRepository: TrendingStatusRepository,
) : FFLocalSource<Status> {
    override suspend fun saveLocally(currentPage: List<PageItem<Status>>) {
        databaseDelegate.withTransaction {
            saveStatusToDatabase(currentPage.map { it.item })
            trendingStatusRepository.saveLocally(currentPage)
        }
    }
}

class TrendingStatusRemoteSource(private val trendingStatusRepository: TrendingStatusRepository) :
    FFRemoteSource<Status> {
    override suspend fun getRemotely(limit: Int, offset: Int): List<Status> {
        return trendingStatusRepository.getRemotely(limit, offset)
    }
}

class TrendingStatusPagingDataFlow(
    private val localSource: TrendingStatusLocalSource,
    private val remoteSource: TrendingStatusRemoteSource,
    private val repository: TrendingStatusRepository,
) {
    @ExperimentalPagingApi
    fun pagingDataFlow(
        pageSize: Int = 40,
        initialLoadSize: Int = 40,
    ): Flow<PagingData<Status>> =
        Pager(
            config =
            PagingConfig(
                pageSize = pageSize,
                initialLoadSize = initialLoadSize,
            ),
            remoteMediator = TrendingStatusRemoteMediator(
                localSource = localSource,
                remoteSource = remoteSource,
            ),
        ) {
            repository.pagingSource()
        }.flow.map { pagingData ->
            pagingData.map {
                it.status.toExternalModel()
            }
        }
}
