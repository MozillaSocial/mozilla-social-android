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

interface FfPager<PAGED_ITEM_TYPE : Any, KEY: Any, DATABASE_OBJECT: Any> {

    fun mapDbObjectToExternalModel(dbo: DATABASE_OBJECT): PAGED_ITEM_TYPE

    @OptIn(ExperimentalPagingApi::class)
    fun getRemoteMediator(): RemoteMediator<KEY, DATABASE_OBJECT>

    fun pagingSource(): PagingSource<KEY, DATABASE_OBJECT>

    @ExperimentalPagingApi
    fun build(
        pageSize: Int = 40,
        initialLoadSize: Int = 40,
    ): Flow<PagingData<PAGED_ITEM_TYPE>> = Pager(
        config = PagingConfig(
            pageSize = pageSize,
            initialLoadSize = initialLoadSize,
        ),
        remoteMediator = getRemoteMediator(),
    ) {
        pagingSource()
    }.flow.map { pagingData -> pagingData.map { mapDbObjectToExternalModel(it) } }
}

interface IndexBasedPager<PAGED_ITEM_TYPE : Any, DATABASE_OBJECT: Any> :
    FfPager<PAGED_ITEM_TYPE, Int, DATABASE_OBJECT> {

    suspend fun saveLocally(items: List<PageItem<PAGED_ITEM_TYPE>>, isRefresh: Boolean)
    suspend fun getRemotely(limit: Int, offset: Int): List<PAGED_ITEM_TYPE>

    @OptIn(ExperimentalPagingApi::class)
    override fun getRemoteMediator(): RemoteMediator<Int, DATABASE_OBJECT> = IndexBasedRemoteMediator(
        saveLocally = ::saveLocally,
        getRemotely = ::getRemotely,
    )
}

interface IdBasedPager<PAGED_ITEM_TYPE : Any, DATABASE_OBJECT: Any> :
    FfPager<PAGED_ITEM_TYPE, Int, DATABASE_OBJECT> {

    suspend fun saveLocally(items: List<PageItem<PAGED_ITEM_TYPE>>, isRefresh: Boolean)
    suspend fun getRemotely(limit: Int, nextKey: String?): MastodonPagedResponse<PAGED_ITEM_TYPE>

    @OptIn(ExperimentalPagingApi::class)
    override fun getRemoteMediator(): RemoteMediator<Int, DATABASE_OBJECT> = IdBasedRemoteMediator(
        saveLocally = ::saveLocally,
        getRemotely = ::getRemotely,
    )
}