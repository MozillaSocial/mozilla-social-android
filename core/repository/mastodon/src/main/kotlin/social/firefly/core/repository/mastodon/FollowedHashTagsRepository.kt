package social.firefly.core.repository.mastodon

import social.firefly.core.database.dao.FollowedHashTagsDao
import social.firefly.core.database.model.entities.hashtagCollections.FollowedHashTag
import social.firefly.core.model.HashTag
import social.firefly.core.model.paging.MastodonPagedResponse
import social.firefly.core.network.mastodon.FollowedTagsApi
import social.firefly.core.repository.mastodon.model.hashtag.toExternalModel
import social.firefly.core.repository.mastodon.model.toMastodonPagedResponse

class FollowedHashTagsRepository(
    private val api: FollowedTagsApi,
    private val dao: FollowedHashTagsDao,
) {
    fun pagingSource() = dao.pagingSource()

    suspend fun getFollowedHashTagsPage(
        maxId: String? = null,
        sinceId: String? = null,
        minId: String? = null,
        limit: Int? = null,
    ): MastodonPagedResponse<HashTag> = api.getFollowedHashTags(
        maxId = maxId,
        sinceId = sinceId,
        minId = minId,
        limit = limit,
    ).toMastodonPagedResponse { it.toExternalModel() }

    suspend fun insertAll(data: List<FollowedHashTag>) {
        dao.upsertAll(data)
    }

    suspend fun deleteAll() {
        dao.deleteAll()
    }
}