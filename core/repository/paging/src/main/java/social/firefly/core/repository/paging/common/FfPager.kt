package social.firefly.core.repository.paging.common

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.map
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import social.firefly.core.database.model.entities.statusCollections.TrendingStatusWrapper
import social.firefly.core.model.Status
import social.firefly.core.repository.common.FFLocalSource
import social.firefly.core.repository.common.FFRemoteSource
import social.firefly.core.repository.common.PageItem
import social.firefly.core.repository.common.PagingSourceProvider

interface FfPager<T : Any, DBO: Any>:  FFLocalSource<T>, FFRemoteSource<T>,
    PagingSourceProvider<DBO> {

    fun map(dbo: DBO): T

    @ExperimentalPagingApi
    fun build(
        pageSize: Int = 40,
        initialLoadSize: Int = 40,
    ): Flow<PagingData<T>> =
        Pager(
            config =
            PagingConfig(
                pageSize = pageSize,
                initialLoadSize = initialLoadSize,
            ),
            remoteMediator = FfRemoteMediator<T, DBO>(
                localSource = this,
                remoteSource = this,
            ),
        ) {
            this.pagingSource()
        }.flow.map { pagingData -> pagingData.map { map(it) } }
}