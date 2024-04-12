package social.firefly.core.repository.mastodon

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.RemoteMediator
import androidx.paging.map
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import social.firefly.core.database.dao.TrendingHashTagDao
import social.firefly.core.database.model.entities.hashtagCollections.TrendingHashTag
import social.firefly.core.database.model.entities.hashtagCollections.TrendingHashTagWrapper
import social.firefly.core.model.HashTag
import social.firefly.core.network.mastodon.TrendsApi
import social.firefly.core.repository.common.PageItem
import social.firefly.core.repository.mastodon.model.hashtag.toExternalModel

class TrendingHashtagRepository internal constructor(
    private val api: TrendsApi,
    private val dao: TrendingHashTagDao,
) {
    suspend fun getTrendingTags(
        limit: Int? = null,
        offset: Int? = null,
    ): List<HashTag> =
        api.getTrendingTags(limit = limit, offset = offset).map { it.toExternalModel() }

    @ExperimentalPagingApi
    fun getPager(
        remoteMediator: RemoteMediator<Int, TrendingHashTagWrapper>,
        pageSize: Int = 40,
        initialLoadSize: Int = 40,
    ): Flow<PagingData<HashTag>> =
        Pager(
            config =
            PagingConfig(
                pageSize = pageSize,
                initialLoadSize = initialLoadSize,
            ),
            remoteMediator = remoteMediator,
        ) {
            dao.pagingSource()
        }.flow.map { pagingData ->
            pagingData.map {
                it.hashTag.toExternalModel()
            }
        }

    fun deleteAll() {
        dao.deleteAll()
    }

    suspend fun insertAll(data: List<TrendingHashTag>) {
        dao.upsertAll(data)
    }

    suspend fun getRemotely(limit: Int, offset: Int): List<HashTag> = getTrendingTags(
        limit = limit,
        offset = offset
    )

    suspend fun saveLocally(currentPage: List<PageItem<HashTag>>) {
        insertAll(currentPage.map { hashtag ->
            TrendingHashTag(
                hashTagName = hashtag.item.name,
                position = hashtag.position
            )
        })
    }
}