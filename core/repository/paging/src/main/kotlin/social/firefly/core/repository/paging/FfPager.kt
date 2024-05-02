package social.firefly.core.repository.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.RemoteMediator
import androidx.paging.map
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import social.firefly.core.model.PageItem
import social.firefly.core.model.paging.MastodonPagedResponse
import social.firefly.core.repository.paging.remotemediators.generic.IdBasedRemoteMediator
import social.firefly.core.repository.paging.remotemediators.generic.IndexBasedRemoteMediator

interface FfPager<T : Any, KEY: Any, DBO: Any> {

    fun mapDbObjectToExternalModel(dbo: DBO): T

    @OptIn(ExperimentalPagingApi::class)
    fun getRemoteMediator(): RemoteMediator<KEY, DBO>

    fun pagingSource(): PagingSource<KEY, DBO>

    @ExperimentalPagingApi
    fun build(
        pageSize: Int = 40,
        initialLoadSize: Int = 40,
    ): Flow<PagingData<T>> = Pager(
        config = PagingConfig(
            pageSize = pageSize,
            initialLoadSize = initialLoadSize,
        ),
        remoteMediator = getRemoteMediator(),
    ) {
        pagingSource()
    }.flow.map { pagingData -> pagingData.map { mapDbObjectToExternalModel(it) } }
}

interface IndexBasedPager<T : Any, DBO: Any> : FfPager<T, Int, DBO> {
    suspend fun saveLocally(items: List<PageItem<T>>, isRefresh: Boolean)
    suspend fun getRemotely(limit: Int, offset: Int): List<T>

    @OptIn(ExperimentalPagingApi::class)
    override fun getRemoteMediator(): RemoteMediator<Int, DBO> = IndexBasedRemoteMediator(
        saveLocally = ::saveLocally,
        getRemotely = ::getRemotely,
    )
}

interface IdBasedPager<T : Any, DBO: Any> : FfPager<T, Int, DBO> {
    suspend fun saveLocally(items: List<PageItem<T>>, isRefresh: Boolean)
    suspend fun getRemotely(limit: Int, nextKey: String?): MastodonPagedResponse<T>

    @OptIn(ExperimentalPagingApi::class)
    override fun getRemoteMediator(): RemoteMediator<Int, DBO> = IdBasedRemoteMediator(
        saveLocally = ::saveLocally,
        getRemotely = ::getRemotely,
    )
}