package social.firefly.core.repository.mastodon

import social.firefly.core.database.dao.TrendingHashTagDao
import social.firefly.core.database.model.entities.hashtagCollections.TrendingHashTag
import social.firefly.core.model.HashTag
import social.firefly.core.network.mastodon.TrendsApi
import social.firefly.core.repository.mastodon.model.hashtag.toExternalModel

class TrendingHashtagRepository internal constructor(
    private val api: TrendsApi,
    private val dao: TrendingHashTagDao,
) {
    fun pagingSource() = dao.pagingSource()

    suspend fun getTrendingTags(
        limit: Int? = null,
        offset: Int? = null,
    ): List<HashTag> = api.getTrendingTags(
        limit = limit,
        offset = offset
    ).map { it.toExternalModel() }

    suspend fun deleteAll() {
        dao.deleteAll()
    }

    suspend fun insertAll(data: List<TrendingHashTag>) {
        dao.upsertAll(data)
    }
}